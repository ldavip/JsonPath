package com.jayway.jsonpath.internal.function.text;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.internal.ParameterizedTest;
import org.junit.Test;

import java.util.Arrays;

public class SplitTest extends ParameterizedTest {

    public SplitTest(Configuration conf) {
        super(conf);
    }

    @Test(expected = JsonPathException.class)
    public void testStringSplitWithoutRegex() {
        verifyFunctionWithArrayResult(conf, "$.text.concat().split()", TEXT_SERIES, null);
    }

    @Test
    public void testStringSplit() {
        Object expected = arrayOf(conf, Arrays.asList("abcdef", "ghijk"));
        verifyFunctionWithArrayResult(conf, "$.concat(\"abcdef\", \" \", \"ghijk\").split(\" \")", TEXT_SERIES, expected);
    }

    @Test
    public void testStringSplitWithRegex() {
        Object expected = arrayOf(conf, Arrays.asList("abc", "def ", " ghi", "jk"));
        verifyFunctionWithArrayResult(conf, "$.concat(\"abc1def\", \" 2 \", \"ghi3jk\").split(\"\\d\")", TEXT_SERIES, expected);
    }

    @Test
    public void testStringSplitWithEmptyString() {
        Object expected = arrayOf(conf, Arrays.asList("a", "b", "c", "d", "e", "f"));
        verifyFunctionWithArrayResult(conf, "$.text.concat().split(\"\")", TEXT_SERIES, expected);
    }

    @Test
    public void testSplitArg() {
        String json = "{ \"text\": \"abcdef ghijkl mnop\" }";
        Object expected = arrayOf(conf, Arrays.asList("abcdef", "ghijkl", "mnop"));
        verifyFunctionWithArrayResult(conf, "$.split(\" \", $.text)", json, expected);
    }

    @Test
    public void testSplit() {
        String json = "{\"codigo\":\"200\",\"mensagem\":\"OK\",\"object\":{\"codigoFamilia\":\"1256874\",\"qtdPessoasCadastradas\":20,\"municipioCadastro\":\"CENTRO\",\"tipoDeLogradouro\":\"DESVIO\",\"tituloLogradouro\":\"DOUTOR\",\"nomeLogradouro\":\"NOME NOME\",\"numeroLogradouro\":10,\"complementoNumero\":\"\",\"complementoAdicional\":\"\",\"cep\":\"85560000\",\"localidade\":\"BARRA VELHA\",\"ufCadastro\":\"SC\",\"ddd\":24,\"listaMembrosApiPositiva\":[{\"cpf\":\"10299751082\",\"nomePessoa\":\"SAULO GALLETO MAZZUCO\",\"grauParentesco\":\"PESSOA RESPONSAVEL PELA UNIDADE FAMILIAR\",\"dataNascimento\":\"1980-01-20\",\"sexo\":\"M\",\"municipioNascimento\":\"MONTE ALEGRE DE SERGIPE\",\"ufNascimento\":\"SE\",\"pais\":\"\"},{\"cpf\":\"57170830094\",\"nomePessoa\":\"CAMILA DOVERLANDIA MAZZUCO\",\"grauParentesco\":\"CONJUGE OU COMPANHEIRO(A)\",\"dataNascimento\":\"1983-02-26\",\"sexo\":\"F\",\"municipioNascimento\":\"CHOPINZINHO\",\"ufNascimento\":\"PR\",\"pais\":\"\"},{\"cpf\":\"46955420054\",\"nomePessoa\":\"JARIBO TAVEDE SISOL\",\"grauParentesco\":\"PAI OU MAE\",\"dataNascimento\":\"1975-01-16\",\"sexo\":\"F\",\"municipioNascimento\":\"BRASILIA\",\"ufNascimento\":\"DF\",\"pais\":\"\"},{\"cpf\":\"75578405002\",\"nomePessoa\":\"VIPUVI GASONE SISOL\",\"grauParentesco\":\"PAI OU MAE\",\"dataNascimento\":\"1963-08-21\",\"sexo\":\"M\",\"municipioNascimento\":\"RIO DE JANEIRO\",\"ufNascimento\":\"RJ\",\"pais\":\"\"},{\"cpf\":\"23739746068\",\"nomePessoa\":\"HONDA MEDINA GALLETO MAZZUCO\",\"grauParentesco\":\"FILHO(A)\",\"dataNascimento\":\"2006-06-10\",\"sexo\":\"F\",\"municipioNascimento\":\"RIO DE JANEIRO\",\"ufNascimento\":\"RJ\",\"pais\":\"\"},{\"cpf\":\"59229774073\",\"nomePessoa\":\"BRUNO SERGIO GALLETO MAZZUCO\",\"grauParentesco\":\"FILHO(A)\",\"dataNascimento\":\"2004-09-16\",\"sexo\":\"M\",\"municipioNascimento\":\"S\\u00c3O PAULO\",\"ufNascimento\":\"SP\",\"pais\":\"\"},{\"cpf\":\"48428327033\",\"nomePessoa\":\"NUFEFO GALLETO MAZZUCO\",\"grauParentesco\":\"FILHO(A)\",\"dataNascimento\":\"2003-11-02\",\"sexo\":\"M\",\"municipioNascimento\":\"IMPERATRIZ\",\"ufNascimento\":\"MA\",\"pais\":\"\"},{\"cpf\":\"95985929019\",\"nomePessoa\":\"NOEMI GALLETO MAZZUCO\",\"grauParentesco\":\"FILHO(A)\",\"dataNascimento\":\"2001-07-30\",\"sexo\":\"M\",\"municipioNascimento\":\"SALVADOR\",\"ufNascimento\":\"BA\",\"pais\":\"\"},{\"cpf\":\"79599119088\",\"nomePessoa\":\"FUBALA GULIBO TESTE\",\"grauParentesco\":\"IRMAO OU IRMA\",\"dataNascimento\":\"1991-01-20\",\"sexo\":\"M\",\"municipioNascimento\":\"IMPERATRIZ\",\"ufNascimento\":\"MA\",\"pais\":\"\"},{\"cpf\":\"33777365068\",\"nomePessoa\":\"PUSUFO GETEDA TESTE\",\"grauParentesco\":\"IRMAO OU IRMA\",\"dataNascimento\":\"1990-10-01\",\"sexo\":\"F\",\"municipioNascimento\":\"IMPERATRIZ\",\"ufNascimento\":\"MA\",\"pais\":\"\"},{\"cpf\":\"06079574098\",\"nomePessoa\":\"MOCUVE CACADE TESTE\",\"grauParentesco\":\"IRMAO OU IRMA\",\"dataNascimento\":\"1987-06-15\",\"sexo\":\"F\",\"municipioNascimento\":\"IMPERATRIZ\",\"ufNascimento\":\"MA\",\"pais\":\"\"}],\"telefone\":\"36366482\"}}";
        String pathExpr = "$..listaMembrosApiPositiva[?(@.grauParentesco == 'FILHO(A)' && @.dataNascimento == $..listaMembrosApiPositiva[?(@.grauParentesco == 'FILHO(A)')].dataNascimento.maxDate(\"yyyy-MM-dd\"))].nomePessoa.first().split(\" \").get(0)";
        verifyFunction(conf, pathExpr, json, "HONDA");
    }

}
