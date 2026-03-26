package at.spengergasse.spring_thymeleaf.controllers;

import at.spengergasse.spring_thymeleaf.entities.Device;
import at.spengergasse.spring_thymeleaf.entities.DeviceRepository;
import at.spengergasse.spring_thymeleaf.entities.DeviceType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/device")
public class DeviceController {
    private final DeviceRepository deviceRepository;

    public DeviceController(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @GetMapping("/list")
    public String devices(Model model) {
        model.addAttribute("devices", deviceRepository.findAll());
        return "devicelist";
    }

    @GetMapping("/add")
    public String addDevice(Model model) {
        model.addAttribute("device", new Device());
        model.addAttribute("types", DeviceType.values());
        return "add_device";
    }

    @PostMapping("/add")
    public String addDevice(@ModelAttribute("device") Device device) {
        deviceRepository.save(device);
        return "redirect:/device/list";
    }

    @GetMapping("/delete")
    public String deleteDevice(@RequestParam int id) {
        deviceRepository.deleteById(id);
        return "redirect:/device/list";
    }

    @GetMapping("/edit")
    public String editDevice(@RequestParam int id, Model model) {
        Device d = deviceRepository.findById(id).orElseThrow();
        model.addAttribute("device", d);
        model.addAttribute("types", DeviceType.values());
        return "add_device";
    }
}
