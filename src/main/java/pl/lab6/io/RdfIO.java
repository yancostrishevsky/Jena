package pl.lab6.io;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

public final class RdfIO {
    private RdfIO() {
    }

    public static Model loadModel(String path) {
        Model model = ModelFactory.createDefaultModel();
        RDFDataMgr.read(model, path);
        return model;
    }

    public static void writeModel(Model model, String path, Lang lang) throws IOException {
        try (OutputStream out = new FileOutputStream(path)) {
            RDFDataMgr.write(out, model, lang);
        }
    }

    public static Lang parseLang(String format) {
        if (format == null) {
            return Lang.TTL;
        }

        String normalized = format.trim().toUpperCase(Locale.ROOT);
        switch (normalized) {
            case "TTL":
                return Lang.TTL;
            case "RDFXML":
            case "RDF_XML":
            case "RDF/XML":
                return Lang.RDFXML;
            case "JSONLD":
            case "JSON_LD":
                return Lang.JSONLD;
            default:
                throw new IllegalArgumentException("Unsupported format: " + format);
        }
    }
}
