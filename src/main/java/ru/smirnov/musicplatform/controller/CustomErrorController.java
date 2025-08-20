//package ru.smirnov.musicplatform.controller;
//
//import jakarta.servlet.http.HttpServletRequest;
//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import ru.smirnov.musicplatform.dto.exception.ExceptionDto;
//import ru.smirnov.musicplatform.exception.InvalidTokenException;
//
//@RestController
//public class CustomErrorController implements ErrorController {
//
//    // насколько я понял: если в цепочке вызова, в которой возникает исключение, нет контроллера, то в
//    // глобальный контроллер-обработчик исключений (@ControllerAdvice) это исключение не обработается
//
//    @RequestMapping("/error")
//    public ResponseEntity<ExceptionDto> handleFilterExceptions(HttpServletRequest request) {
//
//        Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
//
//        if (throwable instanceof InvalidTokenException)
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ExceptionDto(throwable.getMessage()));
//
//
//        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionDto("Unknown exception"));
//
//    }
//
//}
