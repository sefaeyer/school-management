package com.project.controller.business;

import com.project.payload.request.business.LessonRequest;
import com.project.payload.response.business.LessonResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final LessonService lessonService;


    // SAVE  ********************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @PostMapping("/save") // http://localhost:8080/lessons/save + POST + JSON
    public ResponseMessage<LessonResponse> saveLesson(@RequestBody @Valid LessonRequest lessonRequest){

        return lessonService.saveLesson(lessonRequest);
    }




    // ODEV  -->  deleteById;
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @DeleteMapping("/delete/{id}") // http://localhost:8080/lessons/delete/3 + DELETE
    public ResponseEntity<String> deleteById(@PathVariable Long id ){
        return ResponseEntity.ok(lessonService.deleteLessonById(id));
    }


    // ODEV  -->  getAllWithPage
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER')")
    @GetMapping("/getAll") // http://localhost:8080/lessons/getAll + GET
    public Page<LessonResponse>getAllWithPage(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "startDate") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ){
        return lessonService.getAllWithPage(page, size, sort, type);
    }



    // getLessonByLessonName ***********************************************
    @GetMapping("/getLessonByName") // http://localhost:8080/lessons/getLessonByName?lessonName=java + GET
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<LessonResponse> getLessonByLessonName(@RequestParam String lessonName){
        return lessonService.getLessonByLessonName(lessonName);
    }



    // ODEV  -->  getLessonsByIdList()  ***************************************





    //updateLessonById *******************************************************
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    @PutMapping("/update/{lessonId}") //http://localhost:8080/lessons/update/1
    public ResponseEntity<LessonResponse> updateLessonById(@PathVariable Long lessonId,
                                                           @RequestBody LessonRequest lessonRequest){
        return ResponseEntity.ok(lessonService.updateLessonById(lessonId, lessonRequest));
    }


}
