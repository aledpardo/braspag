package main.java.braspag;

import org.apache.commons.codec.Charsets;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

public class WSBraspag {

    private static final String MERCHANT_KEY = "HTHOWIIRULMGVTTQLYHUUJHXRUVNOWOBZZUQRHHR";
    private static final String MERCHANT_ID = "0acaf365-ffc7-433c-8bea-6f7bbe9d1649";
    private static final String ENDPOINT_SANDBOX = "https://apisandbox.braspag.com.br/v2/";
    private static final String ENDPOINT_QUERY_SANDBOX = "https://apiquerysandbox.braspag.com.br/v2/";
    private static final String SERVICO_VENDAS = "sales";

    public String vendaCartaoCredito() throws Exception {
        final StringBuilder request = new StringBuilder();
        request.append("{   ");
        request.append("   \"MerchantOrderId\":\"118922\", "); // codigo do pedido
        request.append("   \"Customer\":{   "); // Dados do cliente
        request.append("      \"Name\":\"Comprador Teste\", ");
        request.append("      \"Identity\":\"11225468954\", ");
        request.append("      \"IdentityType\":\"CPF\", ");
        request.append("      \"Email\":\"compradorteste@teste.com\", ");
        request.append("      \"Birthdate\":\"1991-01-02\", ");
        request.append("      \"Address\":{   "); // Endereco do cliente
        request.append("         \"Street\":\"Rua Teste\", ");
        request.append("         \"Number\":\"123\", ");
        request.append("         \"Complement\":\"AP 123\", ");
        request.append("         \"ZipCode\":\"12345987\", ");
        request.append("         \"City\":\"Rio de Janeiro\", ");
        request.append("         \"State\":\"RJ\", ");
        request.append("         \"Country\":\"BRA\" ");
        request.append("      }, ");
        request.append("        \"DeliveryAddress\": { "); // Endereco de entrega
        request.append("            \"Street\": \"Rua Teste\", ");
        request.append("            \"Number\": \"123\", ");
        request.append("            \"Complement\": \"AP 123\", ");
        request.append("            \"ZipCode\": \"12345987\", ");
        request.append("            \"City\": \"Rio de Janeiro\", ");
        request.append("            \"State\": \"RJ\", ");
        request.append("            \"Country\": \"BRA\" ");
        request.append("        } ");
        request.append("   }, ");
        request.append("   \"Payment\":{   "); // Tipo de pagamento
        request.append("     \"Type\":\"CreditCard\", ");
        request.append("     \"Amount\":15700, "); // valor
        request.append("     \"Currency\":\"BRL\", ");
        request.append("     \"Country\":\"BRA\", ");
        request.append("     \"Provider\":\"Simulado\", ");
        request.append("     \"ServiceTaxAmount\":0, ");
        request.append("     \"Installments\":1, ");
        request.append("     \"Interest\":\"ByMerchant\", ");
        request.append("     \"Capture\":true, ");
        request.append("     \"Authenticate\":false,     ");
        request.append("     \"Recurrent\": false, ");
        request.append("     \"SoftDescriptor\":\"tst\", ");
        request.append("     \"CreditCard\":{   "); // Dados do cartão do crédito
        request.append("         \"CardNumber\":\"1234123412341231\", ");
        request.append("         \"Holder\":\"Teste Holder\", ");
        request.append("         \"ExpirationDate\":\"12/2021\", ");
        request.append("         \"SecurityCode\":\"123\", ");
        request.append("         \"SaveCard\":\"false\", ");
        request.append("         \"Brand\":\"Visa\" ");
        request.append("     } ");
        request.append("   } ");
        request.append("} ");

        final HttpPost requestPost = new HttpPost(WSBraspag.ENDPOINT_SANDBOX + "/" + WSBraspag.SERVICO_VENDAS);
        this.addHeaders(requestPost);
        requestPost.setEntity(new StringEntity(request.toString()));

        try (CloseableHttpResponse response = HttpClientBuilder.create().build().execute(requestPost)) {
            return IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
        }
    }

    public String vendaComCardToken() throws Exception {
        final StringBuilder request = new StringBuilder();
        request.append("{  ");
        request.append("   \"MerchantOrderId\":\"2014111706\",");
        request.append("   \"Customer\":{  ");
        request.append("      \"Name\":\"Comprador Teste\"     ");
        request.append("   },");
        request.append("   \"Payment\":{  ");
        request.append("     \"Type\":\"CreditCard\",");
        request.append("     \"Amount\":100,");
        request.append("     \"Provider\":\"Simulado\",");
        request.append("     \"Installments\":1,");
        request.append("     \"SoftDescriptor\":\"tst\",");
        request.append("     \"CreditCard\":{  ");
        request.append("         \"CardToken\":\"6e1bf77a-b28b-4660-b14f-455e2a1c95e9\",");
        request.append("         \"SecurityCode\":\"262\",");
        request.append("         \"Brand\":\"Visa\"");
        request.append("     }");
        request.append("   }");
        request.append("}");

        final HttpPost requestPost = new HttpPost(WSBraspag.ENDPOINT_SANDBOX + "/" + WSBraspag.SERVICO_VENDAS);
        this.addHeaders(requestPost);
        requestPost.setEntity(new StringEntity(request.toString().replace("\\", "")));

        try (CloseableHttpResponse response = HttpClientBuilder.create().build().execute(requestPost)) {
            return IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
        }
    }

    public String captura(final String pagamentoId) throws Exception {
        final String queryCapturaPagamento = String.format("/%s/capture?amount=%s&serviceTaxAmount=%s", pagamentoId, "15700", "0");
        final String endPoint = String.format("%s%s%s", WSBraspag.ENDPOINT_SANDBOX, WSBraspag.SERVICO_VENDAS, queryCapturaPagamento);
        final HttpPut requestPut = new HttpPut(endPoint);
        this.addHeaders(requestPut);

        try (CloseableHttpResponse response = HttpClientBuilder.create().build().execute(requestPut)) {
            return IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
        }
    }

    public String estornaVenda(final String pagamentoId) throws Exception {
        final String queryEstornaVenda = String.format("/%s/void?amount=%s", pagamentoId, "15700");
        final String endPoint = String.format("%s%s%s", WSBraspag.ENDPOINT_SANDBOX, WSBraspag.SERVICO_VENDAS, queryEstornaVenda);
        final HttpPut requestPut = new HttpPut(endPoint);
        this.addHeaders(requestPut);

        try (CloseableHttpResponse response = HttpClientBuilder.create().build().execute(requestPut)) {
            return IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
        }
    }

    public String consultaVenda(final String paymentId) throws Exception {
        final String endPoint = String.format("%s%s/%s", WSBraspag.ENDPOINT_QUERY_SANDBOX, WSBraspag.SERVICO_VENDAS, paymentId);
        final HttpGet requestGet = new HttpGet(endPoint);
        this.addHeaders(requestGet);

        try (CloseableHttpResponse response = HttpClientBuilder.create().build().execute(requestGet)) {
            return IOUtils.toString(response.getEntity().getContent(), Charsets.UTF_8);
        }
    }

    public String vendaCartaoDebito() throws Exception {
        return null;
    }

    private void addHeaders(final HttpRequestBase request) {
        request.addHeader("Content-Type", "application/json");
        request.addHeader("MerchantId", WSBraspag.MERCHANT_ID);
        request.addHeader("MerchantKey", WSBraspag.MERCHANT_KEY);
    }
}