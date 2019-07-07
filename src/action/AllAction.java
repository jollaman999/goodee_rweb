package action;

import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REngineException;
import org.rosuda.REngine.Rserve.RConnection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AllAction {
    public String student(HttpServletRequest request, HttpServletResponse response) {
        String wh = request.getParameter("wh");
        String gm = request.getParameter("gm");
        String gr = request.getParameter("gr");
        String col1 = null, col2 = null, str1 = null, str2 = null;
        RConnection c = null;

        int limit = 100;
        if (wh.equals("1")) {
            col1 = "weight";
            str1 = "몸무게";
        } else if (wh.equals("2")) {
            col1 = "height";
            str1 = "키";
            limit = 200;
        }

        if (gm.equals("1")) {
            col2 = "grade";
            str2 = "학년";
        } else if (gm.equals("2")) {
            col2 = "major1";
            str2 = "학과";
        }

        String sql = "select " + col2 + ", name, avg(" +
                    col1 + ") from student group by " + col2;

        try {
            c = new RConnection();
            c.parseAndEval("library(RJDBC)");
            c.parseAndEval("drv=JDBC(driverClass='org.mariadb.jdbc.Driver'," +
                    "classPath='G:/rweb/web/WEB-INF/lib/mariadb-java-client-2.4.1.jar')"
            );
            c.parseAndEval("conn=dbConnect(drv, 'jdbc:mariadb://localhost:3306/bigdb', 'scott', 'tiger')");
            c.parseAndEval("rst=dbGetQuery(conn, '" + sql + "')");
            if (gr.equals("1")) { // 막대 그래프
                c.parseAndEval("bp = barplot(rst$avg," +
                        " main = '" + str2 + "별 학생 평균 " + str1 + "'," +
                        " col = rainbow(10), cex.names = 0.8," +
                        " ylim = c(0, " + limit + "), names.arg = rst$name," +
                        " las = 1, ylab = '" + str1 + "', xlab = '" + str2 + "')");
                c.parseAndEval("abline(h = seq(0, " + limit + ", 10), lty = 2)");
            } else if (gr.equals("2")) { // 파이 그래프
                c.parseAndEval("bp = pie(rst$avg," +
                        " main = '" + str2 + "별 학생 평균 " + str1 + "'," +
                        " col = rainbow(10), cex = 0.8," +
                        " labels = rst$name)");
            }
            String path = request.getServletContext().getRealPath("/") + "img/student.png";
            path = path.replace("\\", "/");
            c.parseAndEval("savePlot('" + path + "', type='png')");
            c.parseAndEval("dev.off()");
        } catch (REXPMismatchException | REngineException e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return null;
    }
}
