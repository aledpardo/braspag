package test.java.braspag;

import org.junit.Assert;
import org.junit.Test;

import com.google.gson.Gson;

import main.java.braspag.WSBraspag;

public class WSBraspagTest {

    @Test
    public void testaVendaComCartaoDeCredito() throws Exception {
        final String vendaResposta = new WSBraspag().vendaCartaoCredito();
        Assert.assertTrue(vendaResposta != null);
    }

    @Test
    public void testaVendaComCardToken() throws Exception {
        final String vendaComCardToken = new WSBraspag().vendaComCardToken();
        Assert.assertTrue(vendaComCardToken != null);
    }

    @Test
    public void testaCaptura() throws Exception {
        final WSBraspag wsBraspag = new WSBraspag();

        final String vendaString = wsBraspag.vendaCartaoCredito();
        final RespostaVenda resposta = new Gson().fromJson(vendaString, RespostaVenda.class);
        final String capturaString = wsBraspag.captura(resposta.getPayment().getPaymentId());
        Assert.assertTrue(capturaString != null);
    }

    @Test
    public void testaEstorno() throws Exception {
        final WSBraspag wsBraspag = new WSBraspag();

        final String vendaString = wsBraspag.vendaCartaoCredito();
        final RespostaVenda resposta = new Gson().fromJson(vendaString, RespostaVenda.class);
        final String estornoString = wsBraspag.estornaVenda(resposta.getPayment().getPaymentId());
        Assert.assertTrue(estornoString != null);
    }

    @Test
    public void testaConsulta() throws Exception {
        final WSBraspag wsBraspag = new WSBraspag();
        final String vendaString = wsBraspag.vendaCartaoCredito();
        final RespostaVenda resposta = new Gson().fromJson(vendaString, RespostaVenda.class);
        final String estornoString = wsBraspag.consultaVenda(resposta.getPayment().getPaymentId());

        Assert.assertTrue(estornoString != null);
    }

    @Test
    public void testaPagamentoCartaoDebito() throws Exception {
        final WSBraspag wsBraspag = new WSBraspag();
        final String resposta = wsBraspag.vendaCartaoDebito();
        Assert.assertTrue(resposta != null);
    }

    class RespostaVenda {
        private Payment Payment;

        public Payment getPayment() {
            return this.Payment;
        }
    }

    class Payment {
        private String PaymentId;

        public String getPaymentId() {
            return this.PaymentId;
        }
    }
}