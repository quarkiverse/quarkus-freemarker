package io.quarkiverse.it.freemarker;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import freemarker.template.Configuration;
import freemarker.template.TemplateException;

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
