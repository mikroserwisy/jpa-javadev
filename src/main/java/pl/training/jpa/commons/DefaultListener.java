package pl.training.jpa.commons;

import lombok.extern.java.Log;

@Log
public class DefaultListener {

    public void postLoad(Object entity) {
        log.info("postLoad");
    }

}
