package pl.lab6;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.apache.jena.rdf.model.Model;
import org.junit.jupiter.api.Test;

import pl.lab6.io.RdfIO;

public class RdfIOTest {
    @Test
    void loadsModelFromFile() {
        Model model = RdfIO.loadModel("data/wardrobe.ttl");
        assertTrue(model.size() > 0, "Model should contain triples");
    }
}
