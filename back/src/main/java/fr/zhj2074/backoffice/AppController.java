package fr.zhj2074.backoffice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
public class AppController {

    @Autowired
    private Environment env;

    @RequestMapping(method = GET, value = "/version")
    public String getVersion() {
        return env.getProperty("info.build.version");
    }
}
