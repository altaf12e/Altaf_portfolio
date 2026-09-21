package com.portfolio.app.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes) {
        log.warn("Uploaded file exceeds maximum allowed size: {}", exc.getMessage());
        redirectAttributes.addFlashAttribute("resumeErrorMessage",
                "Uploaded file is too large! Maximum allowed file size is 15MB.");
        return "redirect:/admin/settings#resume-section";
    }
}

