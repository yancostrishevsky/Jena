package pl.lab6.query;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.query.ResultSetFormatter;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

public final class SparqlRunner {
    private SparqlRunner() {
    }

    public static void runQuery(Model model, String queryPath) {
        Query query = QueryFactory.read(queryPath);
        try (QueryExecution qexec = QueryExecutionFactory.create(query, model)) {
            if (query.isSelectType()) {
                ResultSet results = qexec.execSelect();
                ResultSetFormatter.out(System.out, results, query);
                return;
            }
            if (query.isAskType()) {
                boolean result = qexec.execAsk();
                System.out.println("ASK result: " + result);
                return;
            }
            if (query.isConstructType()) {
                Model out = qexec.execConstruct();
                RDFDataMgr.write(System.out, out, Lang.TTL);
                return;
            }
            if (query.isDescribeType()) {
                Model out = qexec.execDescribe();
                RDFDataMgr.write(System.out, out, Lang.TTL);
                return;
            }
            System.out.println("Unsupported SPARQL query type.");
        }
    }
}
