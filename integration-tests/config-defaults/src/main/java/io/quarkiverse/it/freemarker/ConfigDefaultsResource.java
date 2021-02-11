package io.quarkiverse.it.freemarker;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Map;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.quarkiverse.freemarker.TemplatePath;

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
