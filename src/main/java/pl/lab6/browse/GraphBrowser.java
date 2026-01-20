package pl.lab6.browse;

import java.util.HashSet;
import java.util.Set;

import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.vocabulary.RDF;

public final class GraphBrowser {
    private static final String NS = "http://example.org/wardrobe#";

    public void printOverview(Model model) {
        long triples = model.size();
        long subjects = countSubjects(model);

        System.out.println("Triples: " + triples);
        System.out.println("Subjects: " + subjects);
        System.out.println();

        System.out.println("Sample triples:");
        printSample(model, 10);
        System.out.println();

        System.out.println("Instances of Clothing:");
        printClothingInstances(model);
    }

    private long countSubjects(Model model) {
        Set<Resource> subjects = new HashSet<>();
        ResIterator iter = model.listSubjects();
        while (iter.hasNext()) {
            subjects.add(iter.nextResource());
        }
        return subjects.size();
    }

    private void printSample(Model model, int limit) {
        StmtIterator iter = model.listStatements();
        int count = 0;
        while (iter.hasNext() && count < limit) {
            Statement st = iter.nextStatement();
            System.out.println("  " + formatStatement(model, st));
            count++;
        }
        if (count == 0) {
            System.out.println("  (none)");
        }
    }

    private void printClothingInstances(Model model) {
        Resource clothingClass = model.createResource(NS + "Clothing");
        ResIterator iter = model.listResourcesWithProperty(RDF.type, clothingClass);
        int count = 0;
        while (iter.hasNext()) {
            Resource res = iter.nextResource();
            System.out.println("  " + formatNode(model, res));
            count++;
        }
        if (count == 0) {
            System.out.println("  (none)");
        }
    }

    private String formatStatement(Model model, Statement st) {
        String subject = formatNode(model, st.getSubject());
        Property predicate = st.getPredicate();
        String pred = model.shortForm(predicate.getURI());
        String object = formatNode(model, st.getObject());
        return subject + " " + pred + " " + object;
    }

    private String formatNode(Model model, RDFNode node) {
        if (node.isURIResource()) {
            return model.shortForm(node.asResource().getURI());
        }
        if (node.isLiteral()) {
            Literal lit = node.asLiteral();
            String lex = lit.getLexicalForm();
            String lang = lit.getLanguage();
            if (lang != null && !lang.isEmpty()) {
                return '"' + lex + "@" + lang + '"';
            }
            if (lit.getDatatypeURI() != null) {
                return '"' + lex + "^^" + model.shortForm(lit.getDatatypeURI()) + '"';
            }
            return '"' + lex + '"';
        }
        return node.toString();
    }
}
