package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
                                  @RequestParam("deviceId") int deviceId) {
        // Resolve associations explicitly to avoid converter setup
        Patient patient = patientRepository.findById(patientId).orElseThrow();
        Device device = deviceRepository.findById(deviceId).orElseThrow();
        reservation.setPatient(patient);
        reservation.setDevice(device);
        // Simple guard: ensure end after start
        if (reservation.getEndDateTime().isBefore(reservation.getStartDateTime())) {
            // In a real app, add BindingResult errors. For simplicity, swap times.
            var tmp = reservation.getStartDateTime();
            reservation.setStartDateTime(reservation.getEndDateTime());
            reservation.setEndDateTime(tmp);
        }
        reservationRepository.save(reservation);
        return "redirect:/reservation/list?deviceId=" + reservation.getDevice().getId();
    }

    @GetMapping("/list")
    public String listByDevice(@RequestParam("deviceId") int deviceId, Model model) {
        var device = deviceRepository.findById(deviceId).orElseThrow();
        model.addAttribute("device", device);
        model.addAttribute("reservations", reservationRepository.findByDeviceIdOrderByStartDateTimeAsc(deviceId));
        return "reservation_list";
    }
}
