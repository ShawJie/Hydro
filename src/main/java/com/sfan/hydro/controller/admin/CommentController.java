package com.sfan.hydro.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/comment")
public class CommentController {

    private final String PREFIX_PATH = "/admin/comment/";

    @RequestMapping
    public String commentMain(){
        return PREFIX_PATH + "commentMain";
    }
}
