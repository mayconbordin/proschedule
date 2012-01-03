package com.proschedule.core.persistence.view.component;

import java.util.ArrayList;
import com.proschedule.core.persistence.facade.ComponentFacade;
import com.proschedule.core.persistence.exceptions.ComponentDetailPersistenceException;
import com.proschedule.core.persistence.exceptions.ComponentPersistenceException;
import com.proschedule.core.persistence.exceptions.OperationPersistenceException;
import com.proschedule.core.persistence.facade.OperationFacade;
import com.proschedule.core.persistence.model.Operation;
import com.proschedule.core.persistence.model.Component;
import com.proschedule.core.persistence.model.ComponentDetail;
import com.proschedule.util.search.SearchParam;
import com.proschedule.validator.util.ValidatorException;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * O Controlador de Componentes fica responsável pelo controle dos dados dos componentes.
 * Isso inclui a ligação m:n com os detalhes do componentes. Basicamente existem
 * quatro tipos de casos que serão abordados neste teste, são essas as situações
 * que devem ser tratadas pela classe controller:
 *
 * Caso 1: Novo Componente Novos Detalhes
 * Caso 2: Novo Componente Edição de Detalhes
 * Caso 3: Edição de Componente Novo Detalhes
 * Caso 4: Edição de Componente Edição de Detalhes
 *
 * Obs.: Os métodos que efetuam os testes não estão dividios entre os quatro casos,
 * não de forma expressamente delimitada, mas todas as situações estão aqui representadas.
 *
 * @author Maycon Bordin
 */
public class ComponentControllerTest {
    private ComponentController controller;

    public ComponentControllerTest() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Before
    public void setUp() {
        controller = new ComponentController();
    }

    @After
    public void tearDown() {
    }

    /**
     * Caso 1: Novo Componente Novos Detalhes
     * Caso 2: Novo Componente Edição de Detalhes
     */
    @Test
    public void testNewComponent() {
        try {
            System.out.println("newComponent");
            
            //Novo Componente
            controller.newComponent();
            controller.setComponentId("TESTE");
            controller.setComponentRawMaterial("10 x 10 mm");

            //Novo Detalhe
            Operation o = new OperationFacade().list().get(0);
            controller.newComponentDetail();
            controller.setComponentDetailOrder(1);
            controller.setComponentDetailOperation(o.getId());
            controller.setComponentDetailLeadTime(10.0, "dias");

            //Salva o novo detalhe
            controller.saveComponentDetail();

            //Verifica se o detalhe foi adicionado ao componente
            int expectedSize = 1;
            int size = controller.getComponent().getDetails().size();
            assertEquals(expectedSize, size);

            //Agora editamos o novo detalhe salvo
            for ( ComponentDetail cd : controller.getComponentDetails() ) {
                cd.setOrder(100);
                controller.saveComponentDetail();
            }

            //Salva o componente
            controller.saveComponent();

            //Pega o componente no banco de dados
            Component c = controller.getComponent("TESTE");

            //Verifica se não é vazio
            if (c == null) {
                fail("Objeto não encontrado.");
            }

            //Garante que o objeto tem a chave correta
            assertEquals("TESTE", c.getId());

            //Verifica novamente quantos detalhes estão armazenados
            expectedSize = 1;
            assertEquals(expectedSize, c.getDetails().size());
            
        } catch (ComponentPersistenceException ex) {
            fail("Erro ao gravar o componente.\n" + ex.getDetailMessage());
        } catch (ComponentDetailPersistenceException ex) {
            fail("Erro ao gravar o detalhe.\n" + ex.getDetailMessage());
        } catch (ValidatorException ex) {
            fail("Erro na validação dos dados.\n");
        } catch (OperationPersistenceException ex) {
            fail("Erro na listagem de operações.\n");
        }
    }

    /**
     * Caso 3: Edição de Componente Novo Detalhes
     * Caso 4: Edição de Componente Edição de Detalhes
     */
    @Test
    public void testEditComponent() {
        try {
            System.out.println("editComponent");

            //Pega o componente no banco de dados
            Component c = controller.getComponent("TESTE");

            //Verifica se não é vazio
            if (c == null) {
                fail("Objeto não encontrado.");
            }

            //Garante que o objeto tem a chave correta
            assertEquals("TESTE", c.getId());

            //Deve haver um detalhe
            int expectedSize = 1;
            assertEquals(expectedSize, c.getDetails().size());

            //Edita o componente
            controller.editComponent(c);

            //Edita o detalhe
            for (ComponentDetail cd : c.getDetails()) {
                controller.editComponentDetail(cd);
                controller.setComponentDetailOrder(50);
                controller.saveComponentDetail();
            }

            //Verifica novamente quantos detalhes estão armazenados
            expectedSize = 1;
            assertEquals(expectedSize, c.getDetails().size());

            //Adiciona mais um detalhe
            Operation o = new OperationFacade().list().get(1);
            controller.newComponentDetail();
            controller.setComponentDetailOrder(2);
            controller.setComponentDetailOperation(o.getId());
            controller.setComponentDetailLeadTime(15.0, "horas");

            //Salva o novo detalhe
            controller.saveComponentDetail();

            //Mais uma verificação de quantidade, agora precisa ter 2 detalhes
            expectedSize = 2;
            assertEquals(expectedSize, c.getDetails().size());

            //Salva o componente
            //Não há nada para salvar, mas é preciso verificar
            //se não há problemas com a lista de detalhes adicionada
            controller.saveComponent();

            //Agora para remover o componente
            //Pega o componente no banco de dados
            c = controller.getComponent("TESTE");

            //Edita o componente
            controller.editComponent(c);

            //Não foi possível usar o iterator duas vezes, por isso
            //foi criada uma lista para fazer a remoção dos detalhes
            List<ComponentDetail> list = new ArrayList(c.getDetails());
            
            //Edita o detalhe, salva e remove
            for (ComponentDetail cd : list) {
                controller.editComponentDetail(cd);
                controller.removeComponentDetail();
            }

            //Remove o componente
            controller.removeComponent();

        } catch (OperationPersistenceException ex) {
            fail("Erro ao listar operações.");
        } catch (ComponentPersistenceException ex) {
            fail("Erro ao gravar o componente.\n" + ex.getDetailMessage());
        } catch (ComponentDetailPersistenceException ex) {
            fail("Erro ao gravar o detalhe.\n" + ex.getDetailMessage());
        } catch (ValidatorException ex) {
            fail("Erro na validação dos dados.\n");
        }
    }
}