package com.example.justlife.services.impl;

import com.example.justlife.dtos.AppointmentDTO;
import com.example.justlife.dtos.CleaningProfessionalDTO;
import com.example.justlife.dtos.ResponseDTO;
import com.example.justlife.exceptions.InvalidArgumentsException;
import com.example.justlife.exceptions.ResourceNotFoundException;
import com.example.justlife.models.AppointmentEntity;
import com.example.justlife.models.CleaningProfessionalEntity;
import com.example.justlife.models.CustomerEntity;
import com.example.justlife.models.VehicleEntity;
import com.example.justlife.repositories.AppointmentRepository;
import com.example.justlife.repositories.CleaningProfessionalRepository;
import com.example.justlife.repositories.CustomerRepository;
import com.example.justlife.repositories.VehicleRepository;
import com.example.justlife.services.AppointmentService;
import com.example.justlife.utils.Constants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AppointmentServiceImpl implements AppointmentService {

    private static final LocalTime WORK_START_TIME = LocalTime.of(8, 0);
    private static final LocalTime WORK_END_TIME = LocalTime.of(22, 0);
    private final AppointmentRepository appointmentRepository;
    private final CleaningProfessionalRepository cleaningProfessionalRepository;
    private final CustomerRepository customerRepository;

    @Override
    public ResponseDTO<List<CleaningProfessionalDTO>> checkAvailability(LocalDate date) {
        log.info("AppointmentServiceImpl :: checkAvailability starts");
        Long startTime = System.currentTimeMillis();
        ResponseDTO<List<CleaningProfessionalDTO>> responseDTO = new ResponseDTO<>();

        List<CleaningProfessionalDTO> availableProfessionals = cleaningProfessionalRepository.findAll().stream()
                .filter(pro -> isAvailableOnDate(pro, date))
                .map(this::convertToDTO)
                .toList();
        responseDTO.setData(availableProfessionals);
        responseDTO.setStatusCode(Constants.STATUS_SUCCESS);

        Long endTime = System.currentTimeMillis();
        log.info("AppointmentServiceImpl :: checkAvailability ends at {}ms", endTime - startTime);
        return responseDTO;
    }

    @Override
    public ResponseDTO<List<CleaningProfessionalDTO>> checkAvailability(LocalDate date, LocalDateTime start, Integer duration) {
        log.info("AppointmentServiceImpl :: checkAvailability with time starts");
        Long startTime = System.currentTimeMillis();
        ResponseDTO<List<CleaningProfessionalDTO>> responseDTO = new ResponseDTO<>();

        List<CleaningProfessionalDTO> availableProfessionals = cleaningProfessionalRepository.findAll().stream()
                .filter(pro -> isAvailableDuringPeriod(pro, start, duration))
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        responseDTO.setData(availableProfessionals);
        responseDTO.setStatusCode(Constants.STATUS_SUCCESS);

        Long endTime = System.currentTimeMillis();
        log.info("AppointmentServiceImpl :: checkAvailabilityMethod ends at {}ms", endTime - startTime);
        return responseDTO;
    }

    @Transactional
    @Override
    public ResponseDTO<AppointmentDTO> createBooking(AppointmentDTO appointmentDTO) {
        log.info("AppointmentServiceImpl :: createBooking starts");
        Long startTime = System.currentTimeMillis();
        ResponseDTO<AppointmentDTO> responseDTO = new ResponseDTO<>();

        if (appointmentDTO.getDuration() == null ||
                appointmentDTO.getCustomer() == null ||
                appointmentDTO.getCleaningProfessionals() == null ||
                appointmentDTO.getStartTime() == null) {
            throw new InvalidArgumentsException("All fields are required");
        }

        if (appointmentDTO.getStartTime().getDayOfWeek() == DayOfWeek.FRIDAY) {
            throw new InvalidArgumentsException("No cleaner works on Friday.");
        }
        if (appointmentDTO.getStartTime().toLocalTime().isBefore(WORK_START_TIME)) {
            throw new InvalidArgumentsException("Appointment start time cannot be before 8 AM.");
        }

        LocalDateTime appointmentEndTime = appointmentDTO.getStartTime().plusHours(appointmentDTO.getDuration());

        if (appointmentEndTime.toLocalTime().isAfter(WORK_END_TIME)) {
            throw new InvalidArgumentsException("Appointment end time cannot be after 10 PM.");
        }

        if (appointmentDTO.getDuration() != 2 && appointmentDTO.getDuration() != 4) {
            throw new InvalidArgumentsException("Appointment duration must be 2 or 4.");
        }

        if (appointmentDTO.getCleaningProfessionals().size() > 3 || appointmentDTO.getCleaningProfessionals().isEmpty()) {
            throw new InvalidArgumentsException("Appointment cleaning professionals must be between 1 and 3.");
        }

        AppointmentEntity appointmentEntity = new AppointmentEntity();
        BeanUtils.copyProperties(appointmentDTO, appointmentEntity);

        CustomerEntity customer = customerRepository.findById(appointmentDTO.getCustomer())
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        List<CleaningProfessionalEntity> professionals = appointmentDTO.getCleaningProfessionals().stream()
                .map(id -> cleaningProfessionalRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Cleaning Professional not found with ID: " + id)))
                .toList();

        if (!areProfessionalsInSameVehicle(professionals)) {
            throw new InvalidArgumentsException("All cleaning professionals must be assigned to the same vehicle.");
        }

        if (!areProfessionalsAvailable(professionals, appointmentDTO.getStartTime(), appointmentDTO.getDuration())) {
            throw new InvalidArgumentsException("One or more cleaning professionals are not available during the selected time.");
        }

        appointmentEntity.setCleaningProfessionals(professionals);
        appointmentEntity.setCustomer(customer);
        appointmentEntity.calculateEndTime();

        AppointmentDTO createdAppointmentDTO = new AppointmentDTO();
        BeanUtils.copyProperties(appointmentRepository.save(appointmentEntity), createdAppointmentDTO);
        appointmentEntity.setId(createdAppointmentDTO.getId());

        updateProfessionalsAvailability(professionals, appointmentEntity);

        responseDTO.setData(createdAppointmentDTO);
        responseDTO.setStatusCode(Constants.STATUS_SUCCESS);

        Long endTime = System.currentTimeMillis();
        log.info("AppointmentServiceImpl :: createBooking ends at {}ms", endTime - startTime);
        return responseDTO;
    }

    @Transactional
    @Override
    public ResponseDTO<AppointmentDTO> updateBooking(AppointmentDTO appointmentDTO) {
        log.info("AppointmentServiceImpl :: updateBooking starts");
        Long startTime = System.currentTimeMillis();
        ResponseDTO<AppointmentDTO> responseDTO = new ResponseDTO<>();

        AppointmentEntity appointmentEntity = appointmentRepository.findById(appointmentDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with ID: " + appointmentDTO.getId()));

        List<CleaningProfessionalEntity> professionals = appointmentDTO.getCleaningProfessionals().stream()
                .map(id -> cleaningProfessionalRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Cleaning Professional not found with ID: " + id)))
                .collect(Collectors.toList());

        if (!areProfessionalsInSameVehicle(professionals)) {
            throw new IllegalArgumentException("All cleaning professionals must be assigned to the same vehicle.");
        }

        if (!areProfessionalsAvailable(professionals, appointmentDTO.getStartTime(), appointmentDTO.getDuration())) {
            throw new InvalidArgumentsException("One or more cleaning professionals are not available during the selected time.");
        }

        appointmentEntity.setCleaningProfessionals(professionals);
        appointmentEntity.setStartTime(appointmentDTO.getStartTime());
        appointmentEntity.setDuration(appointmentDTO.getDuration());
        appointmentEntity.calculateEndTime();

        AppointmentDTO updatedAppointmentDTO = new AppointmentDTO();
        BeanUtils.copyProperties(appointmentRepository.save(appointmentEntity), updatedAppointmentDTO);

        appointmentEntity.setId(updatedAppointmentDTO.getId());
        updateProfessionalsAvailability(professionals, appointmentEntity);

        responseDTO.setData(updatedAppointmentDTO);
        responseDTO.setStatusCode(Constants.STATUS_SUCCESS);

        Long endTime = System.currentTimeMillis();
        log.info("AppointmentServiceImpl :: updateBooking ends at {}ms", endTime - startTime);
        return responseDTO;
    }

    private boolean isAvailableOnDate(CleaningProfessionalEntity pro, LocalDate date) {
        return pro.getAppointments().stream().noneMatch(appointment -> appointment.getStartTime().toLocalDate().isEqual(date));
    }

    private boolean isAvailableDuringPeriod(CleaningProfessionalEntity pro, LocalDateTime startTime, Integer duration) {
        LocalDateTime endTime = startTime.plusHours(duration);
        LocalDateTime breakStartBefore = startTime.minusMinutes(30);
        LocalDateTime breakEndAfter = endTime.plusMinutes(30);

        return pro.getAppointments().stream().noneMatch(appointment ->
                (appointment.getStartTime().isBefore(breakEndAfter) && appointment.getEndTime().isAfter(startTime)) ||
                        (appointment.getStartTime().isBefore(endTime) && appointment.getEndTime().isAfter(breakStartBefore)));
    }


    private boolean areProfessionalsInSameVehicle(List<CleaningProfessionalEntity> professionals) {
        if (professionals.isEmpty()) return true;
        VehicleEntity vehicle = professionals.get(0).getVehicle();
        return professionals.stream().allMatch(pro -> pro.getVehicle().equals(vehicle));
    }

    private boolean areProfessionalsAvailable(List<CleaningProfessionalEntity> professionals, LocalDateTime startTime, Integer duration) {
        return professionals.stream().allMatch(pro -> isAvailableDuringPeriod(pro, startTime, duration));
    }

    private void updateProfessionalsAvailability(List<CleaningProfessionalEntity> professionals, AppointmentEntity appointment) {
        professionals.forEach(pro -> pro.getAppointments().add(appointment));
        cleaningProfessionalRepository.saveAll(professionals);
    }

    private CleaningProfessionalDTO convertToDTO(CleaningProfessionalEntity entity) {
        CleaningProfessionalDTO dto = new CleaningProfessionalDTO();
        BeanUtils.copyProperties(entity, dto);
        List<Integer> appointments = entity.getAppointments().stream()
                .map(AppointmentEntity::getId)
                .toList();
        dto.setAppointments(appointments);
        return dto;
    }
}
