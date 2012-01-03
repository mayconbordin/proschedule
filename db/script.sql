-- Create tables section -------------------------------------------------

-- Table ps_operation

CREATE TABLE ps_operation(
 id Serial NOT NULL,
 description Character varying(80) NOT NULL,
 lead_time_value Double precision NOT NULL,
 lead_time_type Character varying(5) NOT NULL,
 ps_operation_type_id Integer
)
WITH (OIDS=FALSE)
;

COMMENT ON TABLE ps_operation IS 'Armazena as operações da indústria. Componentes e conjuntos são submetidos a operações para serem produzidos.'
;
COMMENT ON COLUMN ps_operation.id IS 'Código sequencial que identifica cada operação.'
;
COMMENT ON COLUMN ps_operation.description IS 'Descrição breve de cada operação.'
;
COMMENT ON COLUMN ps_operation.lead_time_value IS 'O valor do lead time para a operação.'
;
COMMENT ON COLUMN ps_operation.lead_time_type IS 'O tipo de lead time da operação. Ex.: meses, dias, horas, minutos.'
;
COMMENT ON COLUMN ps_operation.ps_operation_type_id IS 'O código do tipo de operação.'
;

-- Add keys for table ps_operation

ALTER TABLE ps_operation ADD CONSTRAINT pk_ps_operation PRIMARY KEY (id)
;

-- Table ps_operation_type

CREATE TABLE ps_operation_type(
 id Serial NOT NULL,
 description Character varying(80) NOT NULL
)
WITH (OIDS=FALSE)
;

COMMENT ON TABLE ps_operation_type IS 'Armazena o tipo de operação. Esta pode ser, por exemplo, uma operação para componentes ou uma operação para conjuntos.'
;
COMMENT ON COLUMN ps_operation_type.id IS 'Código sequencial dos tipos de operações.'
;
COMMENT ON COLUMN ps_operation_type.description IS 'Descrição breve do tipo de operação.'
;

-- Add keys for table ps_operation_type

ALTER TABLE ps_operation_type ADD CONSTRAINT pk_ps_operation_type PRIMARY KEY (id)
;

-- Table ps_set

CREATE TABLE ps_set(
 id Character varying(50) NOT NULL,
 lead_time_value Double precision NOT NULL,
 lead_time_type Character varying(5) NOT NULL
)
WITH (OIDS=FALSE)
;

COMMENT ON TABLE ps_set IS 'Armazena os conjuntos que são compostos por componentes e passam por operações.'
;
COMMENT ON COLUMN ps_set.id IS 'Código do conjunto.'
;
COMMENT ON COLUMN ps_set.lead_time_value IS 'Valor do lead time para componentes.'
;
COMMENT ON COLUMN ps_set.lead_time_type IS 'Tipo de lead time dos componentes.'
;

-- Add keys for table ps_set

ALTER TABLE ps_set ADD CONSTRAINT pk_ps_set PRIMARY KEY (id)
;

-- Table ps_component

CREATE TABLE ps_component(
 id Character varying(50) NOT NULL,
 raw_material Character varying(80) NOT NULL
)
WITH (OIDS=FALSE)
;

COMMENT ON TABLE ps_component IS 'Armazena os componentes que formão um conjunto e que passam por operações para serem produzidos.'
;
COMMENT ON COLUMN ps_component.id IS 'Código de identificação do componente, este código é proveniente do sistema de MRP Kronus.'
;
COMMENT ON COLUMN ps_component.raw_material IS 'Breve descrição da matéria prima utilizada para fazer o componente.'
;

-- Add keys for table ps_component

ALTER TABLE ps_component ADD CONSTRAINT pk_ps_component PRIMARY KEY (id)
;

-- Table ps_set_detail

CREATE TABLE ps_set_detail(
 ps_set_id Character varying(50) NOT NULL,
 ps_operation_id Integer NOT NULL,
 lead_time_value Double precision NOT NULL,
 lead_time_type Character varying(5) NOT NULL,
 operation_order Integer NOT NULL
)
WITH (OIDS=FALSE)
;

COMMENT ON TABLE ps_set_detail IS 'Armazena as operações pelas quais um conjunto passa durante sua produção, o lead time para essa operação e a ordem em que ocorrerá essa operação.'
;
COMMENT ON COLUMN ps_set_detail.ps_set_id IS 'Código do conjunto.'
;
COMMENT ON COLUMN ps_set_detail.ps_operation_id IS 'Código da operação.'
;

-- Add keys for table ps_set_detail

ALTER TABLE ps_set_detail ADD CONSTRAINT pk_ps_set_operations PRIMARY KEY (ps_set_id,ps_operation_id)
;

-- Table ps_component_detail

CREATE TABLE ps_component_detail(
 ps_operation_id Integer NOT NULL,
 ps_component_id Character varying(50) NOT NULL,
 lead_time_value Double precision NOT NULL,
 lead_time_type Character varying(5) NOT NULL,
 operation_order Integer NOT NULL
)
WITH (OIDS=FALSE)
;

COMMENT ON TABLE ps_component_detail IS 'Armazena as operações pelas quais um componente passa para ser produzido.'
;
COMMENT ON COLUMN ps_component_detail.ps_operation_id IS 'Código da operação.'
;
COMMENT ON COLUMN ps_component_detail.ps_component_id IS 'Código do componente.'
;

-- Add keys for table ps_component_detail

ALTER TABLE ps_component_detail ADD CONSTRAINT pk_ps_component_operations PRIMARY KEY (ps_operation_id,ps_component_id)
;

-- Table ps_set_components

CREATE TABLE ps_set_components(
 ps_set_id Character varying(50) NOT NULL,
 ps_component_id Character varying(50) NOT NULL,
 component_quantity Double precision NOT NULL
)
WITH (OIDS=FALSE)
;

COMMENT ON TABLE ps_set_components IS 'Armazena os componentes que compõe um conjunto.'
;

-- Add keys for table ps_set_components

ALTER TABLE ps_set_components ADD CONSTRAINT pk_ps_set_components PRIMARY KEY (ps_set_id,ps_component_id)
;

-- Table ps_order

CREATE TABLE ps_order(
 id Character varying(50) NOT NULL,
 set_quantity Double precision NOT NULL,
 delivery_date Date NOT NULL,
 ps_set_id Character varying(50) NOT NULL,
 ps_customer_id Character varying(50) NOT NULL,
 day_date Date
)
WITH (OIDS=FALSE)
;

COMMENT ON TABLE ps_order IS 'Armazena as ordens de produção que solicitam a produção de determinada quantidade de um conjunto para ser entregue na data especificada.'
;
COMMENT ON COLUMN ps_order.id IS 'Código da ordem de produção.'
;
COMMENT ON COLUMN ps_order.set_quantity IS 'Quantidade solicitada para o conjunto escolhido.'
;
COMMENT ON COLUMN ps_order.ps_set_id IS 'Código do conjunto solicitado na ordem de produção.'
;
COMMENT ON COLUMN ps_order.ps_customer_id IS 'Código do cliente que solicitou a ordem de produção.'
;

-- Add keys for table ps_order

ALTER TABLE ps_order ADD CONSTRAINT pk_ps_order PRIMARY KEY (id)
;

-- Table ps_customer

CREATE TABLE ps_customer(
 id Character varying(50) NOT NULL,
 name Character varying(80) NOT NULL
)
WITH (OIDS=FALSE)
;

COMMENT ON TABLE ps_customer IS 'Cliente que faz as solicitações de produção.'
;
COMMENT ON COLUMN ps_customer.id IS 'Código do cliente'
;
COMMENT ON COLUMN ps_customer.name IS 'Nome do cliente.'
;

-- Add keys for table ps_customer

ALTER TABLE ps_customer ADD CONSTRAINT pk_ps_customer PRIMARY KEY (id)
;

-- Table ps_order_detail

CREATE TABLE ps_order_detail(
 ps_order_id Character varying(50) NOT NULL,
 ps_component_id Character varying(50) NOT NULL,
 component_quantity Double precision NOT NULL
)
WITH (OIDS=FALSE)
;

COMMENT ON TABLE ps_order_detail IS 'Detalha uma ordem de produção, armazenando informações sobre a quantidade necessária para cada um dos componentes do conjunto.'
;
COMMENT ON COLUMN ps_order_detail.ps_order_id IS 'Código da ordem de produção pertencente.'
;
COMMENT ON COLUMN ps_order_detail.ps_component_id IS 'Código do componente.'
;
COMMENT ON COLUMN ps_order_detail.component_quantity IS 'Quantidade necessária para o componente.'
;

-- Add keys for table ps_order_detail

ALTER TABLE ps_order_detail ADD CONSTRAINT pk_ps_order_detail PRIMARY KEY (ps_order_id,ps_component_id)
;

-- Table ps_calendar

CREATE TABLE ps_calendar(
 calendar_year Integer NOT NULL
)
WITH (OIDS=FALSE)
;

COMMENT ON TABLE ps_calendar IS 'Armazena o calendário anual de dias trabalhados.'
;
COMMENT ON COLUMN ps_calendar.calendar_year IS 'Informa o ano ao qual o calendário pertence.'
;

-- Add keys for table ps_calendar

ALTER TABLE ps_calendar ADD CONSTRAINT pk_ps_calendar PRIMARY KEY (calendar_year)
;

-- Table ps_day

CREATE TABLE ps_day(
 day_date Date NOT NULL,
 week Integer NOT NULL,
 working_day Boolean NOT NULL,
 working_hours Double precision NOT NULL,
 ps_calendar_year Integer NOT NULL
)
WITH (OIDS=FALSE)
;

COMMENT ON TABLE ps_day IS 'Armazena todos os dias do ano, para assim indicar em quais destes dias haverá trabalho.'
;
COMMENT ON COLUMN ps_day.day_date IS 'A data a qual o dia pertence.'
;
COMMENT ON COLUMN ps_day.week IS 'A semana do ano ao qual o dia pertence.'
;
COMMENT ON COLUMN ps_day.working_day IS 'Se for verdadeiro significa que neste dia haverá trabalho, se for falso não haverá trabalho.'
;
COMMENT ON COLUMN ps_day.ps_calendar_year IS 'O calendário ao qual o dia pertence.'
;

-- Add keys for table ps_day

ALTER TABLE ps_day ADD CONSTRAINT pk_ps_day PRIMARY KEY (day_date)
;

-- Table ps_operation_scheduling

CREATE TABLE ps_operation_scheduling(
 ps_operation_id Integer NOT NULL,
 ps_day_date Date NOT NULL
)
WITH (OIDS=FALSE)
;

COMMENT ON TABLE ps_operation_scheduling IS 'Armazena o sequenciamento da produção por dia por operação.'
;
COMMENT ON COLUMN ps_operation_scheduling.ps_operation_id IS 'Código da operação que o sequenciamento está representando.'
;
COMMENT ON COLUMN ps_operation_scheduling.ps_day_date IS 'Dia que o sequenciamento está representando.'
;

-- Add keys for table ps_operation_scheduling

ALTER TABLE ps_operation_scheduling ADD CONSTRAINT pk_ps_operation_scheduling PRIMARY KEY (ps_operation_id,ps_day_date)
;

-- Table ps_operation_scheduling_component_detail

CREATE TABLE ps_operation_scheduling_component_detail(
 ps_operation_id Integer NOT NULL,
 ps_day_date Date NOT NULL,
 ps_component_id Character varying(50) NOT NULL,
 ps_order_id Character varying(50) NOT NULL
)
WITH (OIDS=FALSE)
;

COMMENT ON TABLE ps_operation_scheduling_component_detail IS 'Detalhamento do Sequenciamento por Operação por dia. Indica o componente que será sequenciado e qual a ordem de produção a qual ele pertence. A operação nem precisa ser mencionada, pois este sequenciamento é orientado pelas operações, então se um componente está aqui já subentende-se qual a operação pela qual ele irtá passar. Levando ainda em conta que cabe ao sistema organizar isso de forma correta.'
;
COMMENT ON COLUMN ps_operation_scheduling_component_detail.ps_operation_id IS 'Código da operação.'
;
COMMENT ON COLUMN ps_operation_scheduling_component_detail.ps_day_date IS 'Data do sequenciamento.'
;
COMMENT ON COLUMN ps_operation_scheduling_component_detail.ps_component_id IS 'Código do componente que será sequenciado.'
;
COMMENT ON COLUMN ps_operation_scheduling_component_detail.ps_order_id IS 'Código da ordem de produção a qual o componente pertence, essa ligação também serve para que a quantidade do componente possa ser resgatada.'
;

-- Add keys for table ps_operation_scheduling_component_detail

ALTER TABLE ps_operation_scheduling_component_detail ADD CONSTRAINT pk_ps_operation_scheduling_detail PRIMARY KEY (ps_operation_id,ps_day_date,ps_component_id,ps_order_id)
;

-- Table ps_operation_scheduling_set_detail

CREATE TABLE ps_operation_scheduling_set_detail(
 ps_operation_id Integer NOT NULL,
 ps_day_date Date NOT NULL,
 ps_order_id Character varying(50) NOT NULL,
 ps_set_id Character varying(50) NOT NULL
)
WITH (OIDS=FALSE)
;

-- Add keys for table ps_operation_scheduling_set_detail

ALTER TABLE ps_operation_scheduling_set_detail ADD CONSTRAINT pk_ps_operation_scheduling_set_detail PRIMARY KEY (ps_operation_id,ps_day_date,ps_order_id,ps_set_id)
;

-- Create relationships section ------------------------------------------------- 

ALTER TABLE ps_operation ADD CONSTRAINT fk_opertype_operation FOREIGN KEY (ps_operation_type_id) REFERENCES ps_operation_type (id) ON DELETE NO ACTION ON UPDATE NO ACTION
;

COMMENT ON CONSTRAINT fk_opertype_operation ON ps_operation IS 'Liga um tipo de operação com as operações classificadas neste tipo.'
;

ALTER TABLE ps_set_detail ADD CONSTRAINT fk_set_setdetail FOREIGN KEY (ps_set_id) REFERENCES ps_set (id) ON DELETE NO ACTION ON UPDATE NO ACTION
;

COMMENT ON CONSTRAINT fk_set_setdetail ON ps_set_detail IS 'Liga um conjunto com o detalhamento do mesmo.'
;

ALTER TABLE ps_set_detail ADD CONSTRAINT fk_operation_setdetail FOREIGN KEY (ps_operation_id) REFERENCES ps_operation (id) ON DELETE NO ACTION ON UPDATE NO ACTION
;

COMMENT ON CONSTRAINT fk_operation_setdetail ON ps_set_detail IS 'Liga uma operação com os detalhamentos de conjuntos os quais passam pela operação no processo de produção.'
;

ALTER TABLE ps_component_detail ADD CONSTRAINT fk_operation_componentdetail FOREIGN KEY (ps_operation_id) REFERENCES ps_operation (id) ON DELETE NO ACTION ON UPDATE NO ACTION
;

COMMENT ON CONSTRAINT fk_operation_componentdetail ON ps_component_detail IS 'Liga uma operação com os detalhamentos de componentes que passam pela operação.'
;

ALTER TABLE ps_component_detail ADD CONSTRAINT fk_component_componentdetail FOREIGN KEY (ps_component_id) REFERENCES ps_component (id) ON DELETE NO ACTION ON UPDATE NO ACTION
;

COMMENT ON CONSTRAINT fk_component_componentdetail ON ps_component_detail IS 'Liga um componente com um ou mais detalhamentos do componente, que contém a operação pela qual o componente passa, a ordem e o lead time.'
;

ALTER TABLE ps_set_components ADD CONSTRAINT fk_set_setcomponents FOREIGN KEY (ps_set_id) REFERENCES ps_set (id) ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE ps_set_components ADD CONSTRAINT fk_component_setcomponents FOREIGN KEY (ps_component_id) REFERENCES ps_component (id) ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE ps_order ADD CONSTRAINT fk_set_order FOREIGN KEY (ps_set_id) REFERENCES ps_set (id) ON DELETE NO ACTION ON UPDATE NO ACTION
;

COMMENT ON CONSTRAINT fk_set_order ON ps_order IS 'Liga um conjunto com uma ou mais ordens de produção, indicando qual o conjunto solicitado pela ordem de produção.'
;

ALTER TABLE ps_order ADD CONSTRAINT fk_customer_order FOREIGN KEY (ps_customer_id) REFERENCES ps_customer (id) ON DELETE NO ACTION ON UPDATE NO ACTION
;

COMMENT ON CONSTRAINT fk_customer_order ON ps_order IS 'Liga um cliente a uma ou mais ordens de produção, é o cliente quem é o responsável pela solicitação de um pedido que vem a se transformar em uma ordem de produção.'
;

ALTER TABLE ps_order_detail ADD CONSTRAINT fk_order_orderdetail FOREIGN KEY (ps_order_id) REFERENCES ps_order (id) ON DELETE NO ACTION ON UPDATE NO ACTION
;

COMMENT ON CONSTRAINT fk_order_orderdetail ON ps_order_detail IS 'Liga uma ordem de produção com o detalhamento da mesma, que indica as quantidades necessárias para cada componente do conjunto.'
;

ALTER TABLE ps_order_detail ADD CONSTRAINT fk_component_orderdetail FOREIGN KEY (ps_component_id) REFERENCES ps_component (id) ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE ps_day ADD CONSTRAINT fk_calendar_day FOREIGN KEY (ps_calendar_year) REFERENCES ps_calendar (calendar_year) ON DELETE NO ACTION ON UPDATE NO ACTION
;

COMMENT ON CONSTRAINT fk_calendar_day ON ps_day IS 'Liga um calendário com os dias pertencentes a este calendário.'
;

ALTER TABLE ps_operation_scheduling ADD CONSTRAINT fk_oper_opersched FOREIGN KEY (ps_operation_id) REFERENCES ps_operation (id) ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE ps_operation_scheduling ADD CONSTRAINT fk_day_opersched FOREIGN KEY (ps_day_date) REFERENCES ps_day (day_date) ON DELETE NO ACTION ON UPDATE NO ACTION
;

COMMENT ON CONSTRAINT fk_day_opersched ON ps_operation_scheduling IS 'Liga um dia com os sequenciamentos por operação daquele mesmo dia.'
;

ALTER TABLE ps_operation_scheduling_component_detail ADD CONSTRAINT fk_opersched_operschedcompdetail FOREIGN KEY (ps_operation_id, ps_day_date) REFERENCES ps_operation_scheduling (ps_operation_id, ps_day_date) ON DELETE NO ACTION ON UPDATE NO ACTION
;

COMMENT ON CONSTRAINT fk_opersched_operschedcompdetail ON ps_operation_scheduling_component_detail IS 'Liga um Sequenciamento por Operação por dia com os detalhes de cada um dos componentes que será produzido naquele dia, as quantidades do componente estão na ordem de produção.'
;

ALTER TABLE ps_operation_scheduling_component_detail ADD CONSTRAINT fk_component_operschedcompdetail FOREIGN KEY (ps_component_id) REFERENCES ps_component (id) ON DELETE NO ACTION ON UPDATE NO ACTION
;

COMMENT ON CONSTRAINT fk_component_operschedcompdetail ON ps_operation_scheduling_component_detail IS 'Liga um componente com os detalhamento do sequenciamento por operação por dia, para indicar o componente que será produzido naquele dia naquela operação especifica.'
;

ALTER TABLE ps_operation_scheduling_component_detail ADD CONSTRAINT fk_order_operschedcompdetail FOREIGN KEY (ps_order_id) REFERENCES ps_order (id) ON DELETE NO ACTION ON UPDATE NO ACTION
;

COMMENT ON CONSTRAINT fk_order_operschedcompdetail ON ps_operation_scheduling_component_detail IS 'Liga uma ordem de produção com os detalhamentos do sequenciamento por operação por dia para indicar a qual ordem de produção pertencem os componentes que serão produzidos naquele dia naquela operação.'
;

ALTER TABLE ps_order ADD CONSTRAINT fk_day_order FOREIGN KEY (day_date) REFERENCES ps_day (day_date) ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE ps_operation_scheduling_set_detail ADD CONSTRAINT fk_opersched_operschedsetdetail FOREIGN KEY (ps_operation_id, ps_day_date) REFERENCES ps_operation_scheduling (ps_operation_id, ps_day_date) ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE ps_operation_scheduling_set_detail ADD CONSTRAINT fk_order_operschedsetdetail FOREIGN KEY (ps_order_id) REFERENCES ps_order (id) ON DELETE NO ACTION ON UPDATE NO ACTION
;

ALTER TABLE ps_operation_scheduling_set_detail ADD CONSTRAINT fk_set_operschedsetdetail FOREIGN KEY (ps_set_id) REFERENCES ps_set (id) ON DELETE NO ACTION ON UPDATE NO ACTION
;





