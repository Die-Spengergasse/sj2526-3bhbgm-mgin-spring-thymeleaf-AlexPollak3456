package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reservation")
public class ReservationController {
    private final ReservationRepository reservationRepository;
    private final PatientRepository patientRepository;
    private final DeviceRepository deviceRepository;

    public ReservationController(ReservationRepository reservationRepository,
                                 PatientRepository patientRepository,
                                 DeviceRepository deviceRepository) {
        this.reservationRepository = reservationRepository;
        this.patientRepository = patientRepository;
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/add")
    public String addReservation(Model model) {
        model.addAttribute("reservation", new Reservation());
        model.addAttribute("patients", patientRepository.findAll());
        model.addAttribute("devices", deviceRepository.findAll());
        model.addAttribute("regions", BodyRegion.values());
        return "add_reservation";
    }

    @PostMapping("/add")
    public String saveReservation(@ModelAttribute("reservation") Reservation reservation,
                                  @RequestParam("patientId") int patientId,
                                  @RequestParam("deviceId") int deviceId,
                                  BindingResult result) throws Exception {

        Patient patient = patientRepository.findById(patientId);
        Device device = deviceRepository.findById(deviceId);
        reservation.setPatient(patient);
        reservation.setDevice(device);

        if (reservation.getEndDateTime().isBefore(reservation.getStartDateTime())) {
            var tmp = reservation.getStartDateTime();
            reservation.setStartDateTime(reservation.getEndDateTime());
            reservation.setEndDateTime(tmp);
        }

        if (result.hasErrors()) {
            throw new Exception(result.getAllErrors().get(0).getDefaultMessage());
        }

        List<Reservation> patientReservations = reservationRepository.findByPatientId(patientId);
        for (Reservation r : patientReservations) {
            if (r.getStartDateTime().equals(reservation.getStartDateTime())) {
                throw new IllegalArgumentException("Patient cant have several reservations at the same time");
            }
        }

        List<Reservation> deviceReservations = reservationRepository.findByDeviceId(deviceId);
        for (Reservation r : deviceReservations) {
            if (r.getStartDateTime().equals(reservation.getStartDateTime())) {
                throw new IllegalArgumentException("Machine cant have several reservations at the same time");
            }
        }

        reservationRepository.save(reservation);
        return "redirect:/reservation/list?deviceId=" + reservation.getDevice().getId();
    }

    @GetMapping("/list")
    public String listByDevice(@RequestParam("deviceId") int deviceId, Model model) {
        var device = deviceRepository.findById(deviceId);
        model.addAttribute("device", device);
        model.addAttribute("reservations", reservationRepository.findByDeviceIdOrderByStartDateTimeAsc(deviceId));
        return "reservation_list";
    }

    @ExceptionHandler(Exception.class)
    public String handleException(Exception ex, Model model) {
        model.addAttribute("errorMessage", ex.getMessage());
        return "error";
    }
}