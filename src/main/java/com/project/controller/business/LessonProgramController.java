package com.project.controller.business;

import com.project.payload.request.business.LessonProgramRequest;
import com.project.payload.response.business.LessonProgramResponse;
import com.project.payload.response.business.ResponseMessage;
import com.project.service.business.LessonProgramService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/lessonprograms")
@RequiredArgsConstructor
public class LessonProgramController {

    private final LessonProgramService lessonProgramService;


    @PostMapping("/save") // http://localhost:8080/lessonPrograms/save + POST + JSON
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<LessonProgramResponse> saveLessonProgram(@RequestBody @Valid
                                                                    LessonProgramRequest lessonProgramRequest){

        return lessonProgramService.saveLessonProgram(lessonProgramRequest);
    }


    @GetMapping("/getAll") // http://localhost:8080/lessonPrograms/getAll + GET
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    public List<LessonProgramResponse> getAllLessonProgram(){
        return lessonProgramService.getAllLessonProgram();
    }



    // ODEV -->  getById() ********************************************



    //herhangi bir kullanici atamasi yapilmamis butun dersprogramlari getirecegiz
    @GetMapping("/getAllUnAssigned")  // http://localhost:8080/lessonPrograms/getAllUnAssigned
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER','TEACHER','STUDENT')")
    public List<LessonProgramResponse> getAllUnAssigned(){
        return lessonProgramService.getAllUnassigned();
    }


    // ODEV ->  getAllLessonProgramAssigned () ************************

    // ODEV ->  delete() **********************************************

    // ODEV ->  getAllWithPage() **************************************




    //Bir Ogretmen kendine ait lessonProgramlari getiriyor
    @PreAuthorize("hasAnyAuthority('TEACHER')")
    @GetMapping("/getAllLessonProgramByTeacher") // http://localhost:8080/lessonPrograms/getAllLessonProgramByTeacher
    public Set<LessonProgramResponse> getAllLessonProgramByTeacher(HttpServletRequest httpServletRequest){

        return lessonProgramService.getAllLessonProgramByUser(httpServletRequest);

    }


    //Bir Ogrenci kendine ait lessonProgramlari getiriyor
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    @GetMapping("/getAllLessonProgramByStudent") // http://localhost:8080/lessonPrograms/getAllLessonProgramByStudent
    public Set<LessonProgramResponse> getAllLessonProgramByStudent(HttpServletRequest httpServletRequest){

        return lessonProgramService.getAllLessonProgramByUser(httpServletRequest);

    }

}
