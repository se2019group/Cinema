package com.example.cinema.controller.user;

import com.example.cinema.bl.user.CinemaMemberService;
import com.example.cinema.vo.CinemaMemberForm;
import com.example.cinema.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/cinemaMember")
public class CinemaMemberController {
    @Autowired
    private CinemaMemberService cinemaMemberService;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseVO insertCinemaMember(@RequestBody CinemaMemberForm cinemaMemberForm){
        return cinemaMemberService.addMember(cinemaMemberForm);
    }

    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ResponseVO getAllMember(){
        return cinemaMemberService.getAllMember();
    }

    @PostMapping("/delete")
    public ResponseVO deleteMember(@RequestParam int userId){
        return cinemaMemberService.fireMember(userId);
    }
    @RequestMapping(value = "/update",method = RequestMethod.POST)
    public ResponseVO updateMember(@RequestBody CinemaMemberForm cinemaMemberForm) {
        return cinemaMemberService.updateMember(cinemaMemberForm);
    }
}
