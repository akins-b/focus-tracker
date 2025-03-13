package com.boma.focus.tracker.controller;

import com.boma.focus.tracker.model.FocusSession;
import com.boma.focus.tracker.service.FocusSessionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/focus-session")
public class FocusSessionController {
    private final FocusSessionService focusSessionService;

    public FocusSessionController(FocusSessionService focusSessionService) {
        this.focusSessionService = focusSessionService;
    }

    @PostMapping("/start")
    public FocusSession startFocusSession() {
        return focusSessionService.startFocusSession();
    }

    @GetMapping("test")
    public String stringReturn(HttpServletRequest request){
        return "Hello " + request.getSession().getId();
    }

    @GetMapping("/test1")
    public  String test1(){
        return "hello";
    }
//    @PostMapping("/end")
//    public FocusSession endFocusSession(@RequestBody FocusSession focusSession) {
//        return focusSessionService.endFocusSession(focusSession);
//    }
}
