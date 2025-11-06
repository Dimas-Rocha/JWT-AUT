package controller;

import org.springframework.boot.availability.AvailabilityChangeEvent;
import org.springframework.boot.availability.LivenessState;
import org.springframework.boot.availability.ReadinessState;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/management")
@PreAuthorize("hasRole('ADMIN')")
public class ManagementController {
    
    private final ApplicationEventPublisher eventPublisher;
    
    public ManagementController(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }
    
    @PostMapping("/liveness/{state}")
    public ResponseEntity<String> changeLiveness(@PathVariable String state) {
        switch (state.toLowerCase()) {
            case "correct":
                AvailabilityChangeEvent.publish(eventPublisher, this, LivenessState.CORRECT);
                break;
            case "broken":
                AvailabilityChangeEvent.publish(eventPublisher, this, LivenessState.BROKEN);
                break;
            default:
                return ResponseEntity.badRequest().body("Invalid state. Use 'correct' or 'broken'");
        }
        return ResponseEntity.ok("Liveness state changed to: " + state);
    }
    
    @PostMapping("/readiness/{state}")
    public ResponseEntity<String> changeReadiness(@PathVariable String state) {
        switch (state.toLowerCase()) {
            case "accepting_traffic":
                AvailabilityChangeEvent.publish(eventPublisher, this, ReadinessState.ACCEPTING_TRAFFIC);
                break;
            case "refusing_traffic":
                AvailabilityChangeEvent.publish(eventPublisher, this, ReadinessState.REFUSING_TRAFFIC);
                break;
            default:
                return ResponseEntity.badRequest().body("Invalid state. Use 'accepting_traffic' or 'refusing_traffic'");
        }
        return ResponseEntity.ok("Readiness state changed to: " + state);
    }
}