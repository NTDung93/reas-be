//package vn.fptu.reasbe.controller;
//
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//import vn.fptu.reasbe.model.dto.core.BaseSearchPaginationResponse;
//
//@RestController
//@RequestMapping("/api/v1/critical-report")
//@RequiredArgsConstructor
//public class CriticalReportController {
//
//    private final CriticalReportService criticalReportService;
//
//    @GetMapping("/feedback")
//    @PreAuthorize("hasRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_STAFF)")
//    public ResponseEntity<BaseSearchPaginationResponse<CriticalReportFeedbackResonpse>> getAllFeedbackCriticalReport() {
//        return ResponseEntity.ok(criticalReportService.getAllFeedback());
//    }
//
//    @GetMapping("/user")
//    @PreAuthorize("hasRole(T(vn.fptu.reasbe.model.constant.AppConstants).ROLE_STAFF)")
//    public ResponseEntity<BaseSearchPaginationResponse<CriticalReportFeedbackResonpse>> getAllFeedbackCriticalReport() {
//        return ResponseEntity.ok(criticalReportService.getAllUser());
//    }
//}
