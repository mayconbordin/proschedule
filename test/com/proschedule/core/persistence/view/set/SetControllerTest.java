package com.proschedule.core.persistence.view.set;

import java.util.ArrayList;
import com.proschedule.core.persistence.exceptions.ComponentPersistenceException;
import com.proschedule.core.persistence.exceptions.OperationPersistenceException;
import com.proschedule.core.persistence.exceptions.SetComponentPersistenceException;
import com.proschedule.core.persistence.exceptions.SetPersistenceException;
import com.proschedule.core.persistence.exceptions.SetDetailPersistenceException;
import com.proschedule.validator.util.ValidatorException;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.facade.ComponentFacade;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.persistence.facade.OperationFacade;
import com.proschedule.core.persistence.model.Set;
import com.proschedule.core.persistence.model.SetComponent;
import com.proschedule.core.persistence.model.SetDetail;
import com.proschedule.util.search.SearchParam;
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
 * O Controlador de Conjuntos fica responsável pelo controle dos dados dos conjuntos.
 * Isso inclui a ligação m:n com os detalhes e componentes do conjunto. Basicamente existem
 * quatro tipos de casos que serão abordados neste teste, são essas as situações
 * que devem ser tratadas pela classe controller:
 *
 * Caso 1: Novo Conjunto, Novos Detalhes, Novos Componentes
 * Caso 2: Novo Conjunto, Edição de Detalhes, Edição de Componentes
 * Caso 3: Edição de Conjunto, Novos Detalhes, Novos Componentes
 * Caso 4: Edição de Conjunto, Edição de Detalhes, Edição de Componentes
 *
 * Obs.: Os métodos que efetuam os testes não estão dividios entre os quatro casos,
 * não de forma expressamente delimitada, mas todas as situações estão aqui representadas.
 *
 * @author Maycon Bordin
 */
public class SetControllerTest {
    private SetController controller;

    public SetControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        controller = new SetController();
    }

    @After
    public void tearDown() {
    }

    /**
     * Caso 1: Novo Conjunto, Novos Detalhes, Novos Componentes
     * Caso 2: Novo Conjunto, Edição de Detalhes, Edição de Componentes
     */
    @Test
    public void testNewSet() {
        try {
            System.out.println("newSet");

            //Novo Componente
            controller.newSet();
            controller.setSetId("TESTE");
            ////////////////////////////////////////////////////////////////////

            //Novo Detalhe
            Operation o = new OperationFacade().list().get(0);
            controller.newSetDetail();
            controller.setSetDetailOrder(1);
            controller.setSetDetailOperation(o.getId());
            controller.setSetDetailLeadTime(10.0, "dias");

            //Salva o novo detalhe
            controller.saveSetDetail();

            //Verifica se o detalhe foi adicionado ao componente
            int expectedSize = 1;
            int size = controller.getSet().getDetails().size();
            assertEquals(expectedSize, size);
            ////////////////////////////////////////////////////////////////////

            //Agora editamos o novo detalhe salvo
            for ( SetDetail cd : controller.getSetDetails() ) {
                cd.setOrder(100);
                controller.saveSetDetail();
            }
            ////////////////////////////////////////////////////////////////////

            //Novo componente
            Component c = new ComponentFacade().list().get(0);
            controller.newSetComponent();
            controller.setSetComponentComponent( c.getId() );
            controller.setSetComponentComponentQuantity(1000.0);

            //Salva o componente
            controller.saveSetComponent();

            //Calcula o lead time
            controller.getCalculatedComponentsLeadTimeValue();
            ////////////////////////////////////////////////////////////////////

            //Agora editamos o novo componente salvo
            for ( SetComponent sc : controller.getSetComponents() ) {
                sc.setComponentQuantity(888.0);
                controller.saveSetComponent();
            }

            ////////////////////////////////////////////////////////////////////
            //Salva o componente
            controller.saveSet();

            //Pega o componente no banco de dados
            Set s = controller.getSet("TESTE");

            //Verifica se não é vazio
            if (s == null) {
                fail("Objeto não encontrado.");
            }

            //Garante que o objeto tem a chave correta
            assertEquals("TESTE", s.getId());

            //Verifica novamente quantos detalhes estão armazenados
            expectedSize = 1;
            assertEquals(expectedSize, s.getDetails().size());
            assertEquals(expectedSize, s.getComponents().size());

        } catch (OperationPersistenceException ex) {
            fail("Erro na listagem de operações.\n");
        } catch (SetComponentPersistenceException ex) {
            fail("Erro ao gravar o componente.\n" + ex.getDetailMessage());
        } catch (SetPersistenceException ex) {
            fail("Erro ao gravar o conjunto.\n" + ex.getDetailMessage());
        } catch (SetDetailPersistenceException ex) {
            fail("Erro ao gravar o detalhe.\n" + ex.getDetailMessage());
        } catch (ValidatorException ex) {
            fail("Erro na validação dos dados.\n" + ex.getDetailedMessage());
        } catch (ComponentPersistenceException ex) {
            fail("Erro na listagem de componentes.\n");
        }
    }

    /**
     * Caso 3: Edição de Conjunto, Novos Detalhes, Novos Componentes
     * Caso 4: Edição de Conjunto, Edição de Detalhes, Edição de Componentes
     */
    @Test
    public void testEditSet() {
        try {
            System.out.println("editSet");

            //Pega o conjunto no banco de dados
            Set s = controller.getSet("TESTE");

            //Verifica se não é vazio
            if (s == null) {
                fail("Objeto não encontrado.");
            }

            //Garante que o objeto tem a chave correta
            assertEquals("TESTE", s.getId());

            //Deve haver um detalhe e um componente
            int expectedSize = 1;
            assertEquals(expectedSize, s.getDetails().size());
            assertEquals(expectedSize, s.getComponents().size());
            ////////////////////////////////////////////////////////////////////

            //Edita o componente
            controller.editSet(s);

            //Edita o detalhe
            for (SetDetail cd : s.getDetails()) {
                controller.editSetDetail(cd);
                controller.setSetDetailOrder(50);
                controller.saveSetDetail();
            }

            //Verifica novamente quantos detalhes estão armazenados
            expectedSize = 1;
            assertEquals(expectedSize, s.getDetails().size());

            //Adiciona mais um detalhe
            Operation o = new OperationFacade().list().get(1);
            controller.newSetDetail();
            controller.setSetDetailOrder(2);
            controller.setSetDetailOperation(o.getId());
            controller.setSetDetailLeadTime(15.0, "horas");

            //Salva o novo detalhe
            controller.saveSetDetail();

            //Mais uma verificação de quantidade, agora precisa ter 2 detalhes
            expectedSize = 2;
            assertEquals(expectedSize, s.getDetails().size());

            //Salva o componente
            //Não há nada para salvar, mas é preciso verificar
            //se não há problemas com a lista de detalhes adicionada
            controller.saveSet();
            ////////////////////////////////////////////////////////////////////

            //Edita o componente
            for (SetComponent sc : s.getComponents()) {
                controller.editSetComponent(sc);
                controller.setSetComponentComponentQuantity(500.0);
                controller.saveSetComponent();
            }

            //Verifica novamente quantos componentes estão armazenados
            expectedSize = 1;
            assertEquals(expectedSize, s.getComponents().size());

            //Adiciona mais um detalhe
            Component c = new ComponentFacade().list().get(1);
            controller.newSetComponent();
            controller.setSetComponentComponent( c.getId() );
            controller.setSetComponentComponentQuantity(1000.0);

            //Salva o novo componente
            controller.saveSetComponent();

            //Mais uma verificação de quantidade, agora precisa ter 2 componentes
            expectedSize = 2;
            assertEquals(expectedSize, s.getComponents().size());

            //Salva o componente
            //Não há nada para salvar, mas é preciso verificar
            //se não há problemas com a lista de detalhes adicionada
            controller.saveSet();

            ////////////////////////////////////////////////////////////////////

            //Agora para remover o componente
            //Pega o componente no banco de dados
            s = controller.getSet("TESTE");

            //Edita o componente
            controller.editSet(s);

            //Não foi possível usar o iterator duas vezes, por isso
            //foi criada uma lista para fazer a remoção dos detalhes e componentes
            List<SetDetail> detailList = new ArrayList(s.getDetails());
            List<SetComponent> componentList = new ArrayList(s.getComponents());

            //Edita o detalhe e remove
            for (SetDetail cd : detailList) {
                controller.editSetDetail(cd);
                controller.removeSetDetail();
            }

            //Edita o componente e remove
            for (SetComponent sc : componentList) {
                controller.editSetComponent(sc);
                controller.removeSetComponent();
            }

            //Remove o componente
            controller.removeSet();

        } catch (ComponentPersistenceException ex) {
            fail("Erro ao listar componentes.");
        } catch (SetComponentPersistenceException ex) {
            fail("Erro ao gravar o componente.\n" + ex.getDetailMessage());
        } catch (OperationPersistenceException ex) {
            fail("Erro ao listar operações.");
        } catch (SetPersistenceException ex) {
            fail("Erro ao gravar o conjunto.\n" + ex.getDetailMessage());
        } catch (SetDetailPersistenceException ex) {
            fail("Erro ao gravar o detalhe.\n" + ex.getDetailMessage());
        } catch (ValidatorException ex) {
            fail("Erro na validação dos dados.\n");
        }
    }
}