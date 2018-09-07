package br.ufal.ic.academico.exemplos;

import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Willy
 */
@Slf4j
public class Log {
    
    public void log() {
        log.info("run:config: {}", new Object());

        log.trace("estou aqui {} adsf={} adafd={}", "trace1", "TRACE2", "trace3");
        log.debug("estou aqui >> debug");
        log.info("estou aqui >> info");
        log.warn("estou aqui >> warn");
        log.error("estou aqui >> error");
        
        try {
            throw new UnsupportedOperationException();
        }
        catch (Exception ex) {
            
            log.error("run:problem", ex);
        }
    }
}
