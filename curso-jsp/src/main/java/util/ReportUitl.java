package util;

import java.io.File;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import jakarta.servlet.ServletContext;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@SuppressWarnings({"rawtypes", "unchecked"}) 
public class ReportUitl implements Serializable {

	private static final long serialVersionUID = 1L;

	/*
	 * Este método será responsável por gerar o relatório PDF.
	 * A classe JRBeanCollectionDataSource, segundo a documentação, diz que é uma implementação de data source que contém uma coleção de objetos JavaBean. Ele afirma que
	 * é comum acessar dados de aplicação por meio de camadas de persistência, como EJB, Hibernate... Tais aplicações podem necessitar de gerar relatórios usando dados que
	 * já estão disponíveis como arrays ou collections para os objetos JavaBean na memória.
	 * 
	 * Ou seja, faço a consulta SQL, e com a lista de dados retornada de lá, uso essa classe para trabalhar em cima dela
	 * 
	 * caminhoJasper = getRealPath() vai pegar o caminho real do pacote relatório; file.separator vai ser / ou \, depende do sistema operacional; depois o nome do arquivo
	 * do relatório, e por fim, .jasper.
	 * 
	 * JasperPrint: uma instância dessa classe representa um documento orientado por página que pode ser visto, impresso ou exportado para outros formatos. Ao preencher os
	 * relatórios com dados, o motor produz instâncias dessa classe as quais podem ser transferidas por rede ou exportadas para outros formatos, como PDF, HTML, XLS...
	 * 
	 * JasperFillManager é uma classe de fachada para preencher relatórios compilados com dados vindos de um data source, para produzir documentos orientado por página,
	 * prontos para impressão. Tem vários tipos de retornos e parâmetros a serem passados nele.
	 * 
	 * JasperExportManager é uma classe de fachada para exportar relatórios gerados em formatos populares, como PDF, HTML e XML. Essa classe contém métodos para exportar
	 * nesses 3 formatos somente.
	 */
	public byte[] geraRelatorioPDF(List listaDados, String nomeRelatorio, ServletContext servletContext) throws Exception {
		
		JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(listaDados);
		
		String caminhoJasper = servletContext.getRealPath("relatorio") + File.separator + nomeRelatorio + ".jasper";
		
		JasperPrint impressoraJasper = JasperFillManager.fillReport(caminhoJasper, new HashMap(), source);
		
		return JasperExportManager.exportReportToPdf(impressoraJasper);
	}
}
