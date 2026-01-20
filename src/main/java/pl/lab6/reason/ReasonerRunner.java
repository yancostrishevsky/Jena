package pl.lab6.reason;

import java.io.File;
import java.util.List;

import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;

public final class ReasonerRunner {
    private ReasonerRunner() {
    }

    public static void runAndPrint(Model baseModel, String rulesPath) {
        InfModel infModel = runReasoner(baseModel, rulesPath);
        printInferred(baseModel, infModel);
    }

    private static InfModel runReasoner(Model baseModel, String rulesPath) {
        File rulesFile = new File(rulesPath);
        String rulesUrl = rulesFile.toURI().toString();
        List<Rule> rules = Rule.rulesFromURL(rulesUrl);

        GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
        reasoner.setDerivationLogging(false);

        return ModelFactory.createInfModel(reasoner, baseModel);
    }

    private static void printInferred(Model baseModel, InfModel infModel) {
        Model inferred = infModel.getDeductionsModel();
        if (inferred == null || inferred.isEmpty()) {
            inferred = infModel.difference(baseModel);
        }
        inferred.setNsPrefixes(baseModel.getNsPrefixMap());

        System.out.println("Inferred triples: " + inferred.size());

        StmtIterator iter = inferred.listStatements();
        if (!iter.hasNext()) {
            System.out.println("(none)");
            return;
        }

        while (iter.hasNext()) {
            Statement st = iter.nextStatement();
            System.out.println("  " + formatStatement(inferred, st));
        }
    }

    private static String formatStatement(Model model, Statement st) {
        String subject = formatNode(model, st.getSubject());
        Property predicate = st.getPredicate();
        String pred = model.shortForm(predicate.getURI());
        String object = formatNode(model, st.getObject());
        return subject + " " + pred + " " + object;
    }

    private static String formatNode(Model model, RDFNode node) {
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
