package io.quarkiverse.freemarker.deployment.devmode;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import io.quarkiverse.freemarker.TemplatePath;

@Path("/greeting")
@Produces(MediaType.TEXT_PLAIN)
public class GreetingResource {

    @Inject
    Configuration configuration;

    @Inject
    @TemplatePath("greeting.ftl")
    Template injected;

    @GET
    @Path("/injected")
    public String injected() throws IOException, TemplateException {
        return render(injected);
    }

    @GET
    @Path("/lookup")
    public String lookup() throws IOException, TemplateException {
        return render(configuration.getTemplate("greeting.ftl"));
    }

    private static String render(Template template) throws IOException, TemplateException {
        StringWriter writer = new StringWriter();
        template.process(Map.of("name", "Bob"), writer);
        return writer.toString();
    }
}
