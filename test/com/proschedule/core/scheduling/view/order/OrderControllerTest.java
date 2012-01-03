package com.proschedule.core.scheduling.view.order;

import com.proschedule.core.scheduling.exceptions.OperationSchedulingSetDetailPersistenceException;
import com.proschedule.core.scheduling.model.OperationSchedulingSetDetail;
import com.proschedule.core.scheduling.model.OperationSchedulingComponentDetail;
import com.proschedule.core.scheduling.facade.OperationSchedulingFacade;
import com.proschedule.core.scheduling.facade.OrderFacade;
import com.proschedule.core.persistence.facade.SetFacade;
import com.proschedule.core.persistence.exceptions.ComponentPersistenceException;
import com.proschedule.core.persistence.exceptions.CustomerPersistenceException;
import com.proschedule.core.persistence.exceptions.SetPersistenceException;
import com.proschedule.core.persistence.facade.ComponentFacade;
import com.proschedule.core.scheduling.exceptions.OrderDetailPersistenceException;
import com.proschedule.core.scheduling.exceptions.OrderPersistenceException;
import com.proschedule.util.date.DateUtil;
import com.proschedule.core.persistence.facade.CustomerFacade;
import com.proschedule.core.scheduling.model.Order;
import com.proschedule.core.scheduling.model.OrderDetail;
import com.proschedule.util.search.SearchParam;
import com.proschedule.validator.util.ValidatorException;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * O Controlador de Ordens de Produção fica responsável pelo controle dos dados
 * das ordens de produção.
 * Isso inclui a ligação m:n com os detalhes da ordem de produção. Neste caso os
 * detalhes não podem ser incluidos ou excluidos manualmente, pois eles tem relação
 * direta com o conjunto, portanto é a mudança de conjunto que irá modificar os
 * detalhes da ordem de produção.
 *
 * Caso 1: Nova Ordem de Produção, Novos Detalhes
 * Caso 2: Nova Ordem de Produção, Edição de Detalhes
 * Caso 3: Edição de Ordem de Produção, Novos Detalhes
 * Caso 4: Edição de Ordem de Produção, Edição de Detalhes
 *
 * Obs.: Os métodos que efetuam os testes não estão dividios entre os quatro casos,
 * não de forma expressamente delimitada, mas todas as situações estão aqui representadas.
 *
 * @author Maycon Bordin
 */
public class OrderControllerTest {
    private OrderController controller;

    public OrderControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        controller = new OrderController();
    }

    @After
    public void tearDown() {
    }

    /**
     * Caso 1: Nova Ordem de Produção, Novos Detalhes
     * Caso 2: Nova Ordem de Produção, Edição de Detalhes
     */
    @Test
    public void testNewOrder() {
        try {
            System.out.println("newOrder");

            //Nova Ordem de Produção
            controller.newOrder();
            controller.setOrderId("TESTE");
            controller.setOrderCustomer(new CustomerFacade().list().get(0).getId());
            controller.setOrderDeliveryDate(DateUtil.formatDate("10/10/1999"));
            controller.setOrderSetQuantity(200.0);

            //Novo conjunto - automaticamente puxa os componentes do conjunto
            controller.setOrderSet(new SetFacade().list().get(0).getId(), true);

            //Salva ordem de produção
            controller.saveOrder();

            //verificação
            int size = controller.getOrder().getSet().getComponents().size();
            assertEquals(size, controller.getOrderDetails().size());
            ////////////////////////////////////////////////////////////////////

            //Edição de detalhe
            controller.editOrderDetail( controller.getOrderDetails().get(0) );
            controller.setOrderDetailComponentQuantity(1000.0);
            controller.saveOrderDetail();



        } catch (SetPersistenceException ex) {
            fail("Erro ao recuperar o conjunto.\n" + ex.getDetailMessage());
        } catch (OrderPersistenceException ex) {
            fail("Erro na ordem de produção.\n" + ex.getDetailMessage());
        } catch (ValidatorException ex) {
            fail("Erro ao validar dados.\n" + ex.getDetailedMessage());
        } catch (OrderDetailPersistenceException ex) {
            fail("Erro no detalhe da ordem de produção.\n" + ex.getDetailMessage());
        } catch (CustomerPersistenceException ex) {
            fail("Erro ao recuperar o cliente.\n" + ex.getDetailMessage());
        } catch (Exception ex) {
            fail("Erro desconhecido.\n" + ex);
        }
    }

    /**
     * Caso 3: Edição de Ordem de Produção, Novos Detalhes
     * Caso 4: Edição de Ordem de Produção, Edição de Detalhes
     */
    @Test
    public void testEditOrder() {
        try {
            System.out.println("editOrder");

            if ( controller == null ) {
                System.out.println("NULO");
            }

            //Recupera a ordem e edita-a
            controller.editOrder(controller.getOrder("TESTE"));
            controller.setOrderDeliveryDate( DateUtil.formatDate("20/10/1999"));

            //Salva a ordem
            controller.saveOrder();

            //Verificação
            Order o = controller.getOrder("TESTE");
            if ( !DateUtil.compareDates(DateUtil.formatDate("20/10/1999"), o.getDeliveryDate()) ) {
                fail("As datas deveriam ser iguais");
            }

            //Modifica
            controller.setOrderSet(new SetFacade().list().get(1).getId(), true);
            controller.saveOrder();

            //Verificação
            int size = controller.getOrder().getSet().getComponents().size();
            assertEquals(size, controller.getOrderDetails().size());

            //Remove
            controller.removeAllOrderDetails();
            controller.removeOrder();
            
        } catch (SetPersistenceException ex) {
            fail(ex.getMessage() + ex.getDetailMessage());
        } catch (ValidatorException ex) {
            fail(ex.getMessage() + ex.getDetailedMessage());
        } catch (OrderDetailPersistenceException ex) {
            fail(ex.getMessage() + ex.getDetailMessage());
        } catch (OrderPersistenceException ex) {
            fail(ex.getMessage() + ex.getDetailMessage());
        } catch (Exception ex) {
            fail("Erro desconhecido.\n" + ex);
        } 
    }

}