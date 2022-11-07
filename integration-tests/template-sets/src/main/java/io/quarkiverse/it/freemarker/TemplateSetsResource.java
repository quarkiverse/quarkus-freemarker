package io.quarkiverse.it.freemarker;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/templateSets")
public class TemplateSetsResource {

    @Inject
    Configuration configuration;

    @POST
    @Path("/dynamicTemplate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String dynamicTemplate(Map<String, String> model, @QueryParam("ftl") String ftl)
            throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        configuration.getTemplate(ftl).process(model, stringWriter);
        return stringWriter.toString();
    }

}
