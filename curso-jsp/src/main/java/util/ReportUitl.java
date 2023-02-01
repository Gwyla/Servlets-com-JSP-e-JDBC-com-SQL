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
	 * Este m�todo ser� respons�vel por gerar o relat�rio PDF.
	 * A classe JRBeanCollectionDataSource, segundo a documenta��o, diz que � uma implementa��o de data source que cont�m uma cole��o de objetos JavaBean. Ele afirma que
	 * � comum acessar dados de aplica��o por meio de camadas de persist�ncia, como EJB, Hibernate... Tais aplica��es podem necessitar de gerar relat�rios usando dados que
	 * j� est�o dispon�veis como arrays ou collections para os objetos JavaBean na mem�ria.
	 * 
	 * Ou seja, fa�o a consulta SQL, e com a lista de dados retornada de l�, uso essa classe para trabalhar em cima dela
	 * 
	 * caminhoJasper = getRealPath() vai pegar o caminho real do pacote relat�rio; file.separator vai ser / ou \, depende do sistema operacional; depois o nome do arquivo
	 * do relat�rio, e por fim, .jasper.
	 * 
	 * JasperPrint: uma inst�ncia dessa classe representa um documento orientado por p�gina que pode ser visto, impresso ou exportado para outros formatos. Ao preencher os
	 * relat�rios com dados, o motor produz inst�ncias dessa classe as quais podem ser transferidas por rede ou exportadas para outros formatos, como PDF, HTML, XLS...
	 * 
	 * JasperFillManager � uma classe de fachada para preencher relat�rios compilados com dados vindos de um data source, para produzir documentos orientado por p�gina,
	 * prontos para impress�o. Tem v�rios tipos de retornos e par�metros a serem passados nele.
	 * 
	 * JasperExportManager � uma classe de fachada para exportar relat�rios gerados em formatos populares, como PDF, HTML e XML. Essa classe cont�m m�todos para exportar
	 * nesses 3 formatos somente.
	 */
	public byte[] geraRelatorioPDF(List listaDados, String nomeRelatorio, ServletContext servletContext) throws Exception {
		
		JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(listaDados);
		
		String caminhoJasper = servletContext.getRealPath("relatorio") + File.separator + nomeRelatorio + ".jasper";
		
		JasperPrint impressoraJasper = JasperFillManager.fillReport(caminhoJasper, new HashMap(), source);
		
		return JasperExportManager.exportReportToPdf(impressoraJasper);
	}
}
