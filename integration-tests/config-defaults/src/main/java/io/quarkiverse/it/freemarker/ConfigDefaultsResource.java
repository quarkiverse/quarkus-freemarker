package io.quarkiverse.it.freemarker;

import static jakarta.ws.rs.core.MediaType.TEXT_PLAIN;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.quarkiverse.freemarker.TemplatePath;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/configDefaults")
public class ConfigDefaultsResource {

    @Inject
    Configuration configuration;

    @Inject
    @TemplatePath("greetings.ftl")
    Template greetingsTemplate;

    @GET
    @Path("/injectedTemplate")
    @Produces(TEXT_PLAIN)
    public String injectedTemplate(@QueryParam("name") String name) throws IOException, TemplateException {
        StringWriter stringWriter = new StringWriter();
        greetingsTemplate.process(Collections.singletonMap("name", name), stringWriter);
        return stringWriter.toString();
    }

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
