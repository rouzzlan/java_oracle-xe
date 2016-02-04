DROP
  TABLE ticket;
DROP
  TABLE ticket_response CASCADE CONSTRAINTS;
CREATE
  TABLE ticket_response
  (
    tr_id                CHAR(10) CONSTRAINT pk_ticket_response PRIMARY KEY,
    tr_text              VARCHAR2(255) CONSTRAINT nn_tr_text NOT NULL,
    tr_date              DATE CONSTRAINT nn_tr_date NOT NULL,
    tr_isclient_response CHAR(1) CONSTRAINT chk_is_client_response CHECK(
    tr_isclient_response IN('n', 'j')),
    tr_ticket_id CHAR(10) CONSTRAINT chk_ticket_nr_nn NOT NULL
  );
CREATE
  TABLE ticket
  (
    tc_ticket_number CHAR(10) CONSTRAINT pk_ticket PRIMARY KEY,
    tc_account_id    CHAR(10) CONSTRAINT nn_tc_account_id NOT NULL,
    tc_text          VARCHAR2(255) CONSTRAINT nn_tc_text NOT NULL,
    tc_date_opened   DATE CONSTRAINT nn_tc_date_opened NOT NULL,
    tc_state         NUMBER(1) CONSTRAINT nn_tc_state NOT NULL,
    tc_device_name   VARCHAR(255)
  );
  
ALTER TABLE ticket_response ADD CONSTRAINT fk_ticket_nr FOREIGN KEY (tr_ticket_id) REFERENCES ticket ON DELETE CASCADE;
  
INSERT INTO TICKET VALUES('1', '1', 'Cannot login on webmail', TO_DATE('2012-09-09 13:05:59', 'YYYY-MM-DD HH24:MI:SS'),4, NULL);
INSERT INTO TICKET VALUES('2', '2', 'Have no internet access', TO_DATE('2012-11-05 09:45:13', 'YYYY-MM-DD HH24:MI:SS'),2, NULL);
INSERT INTO TICKET VALUES('3', '3', 'Blue screen!', TO_DATE('2012-12-15 19:15:32', 'YYYY-MM-DD HH24:MI:SS'),1, 'PC-123456');
INSERT INTO TICKET_RESPONSE VALUES('1', 'Account was locked', TO_DATE('2012-09-09 13:24:48', 'YYYY-MM-DD HH24:MI:SS'), 'n', '1');
INSERT INTO TICKET_RESPONSE VALUES('2', 'Account is unlocked, and pwd is reset', TO_DATE('2012-09-09 13:29:11', 'YYYY-MM-DD HH24:MI:SS'), 'n', '1');
INSERT INTO TICKET_RESPONSE VALUES('3', 'Login ok, pwd is changed', TO_DATE('2012-09-10 07:22:36', 'YYYY-MM-DD HH24:MI:SS'), 'j', '1');
INSERT INTO TICKET_RESPONSE VALUES('4', 'Check if cable is plugged in correctly', TO_DATE('2012-11-05 11:25:42', 'YYYY-MM-DD HH24:MI:SS'), 'n', '2');

create or replace PROCEDURE add_ticket(
  p_nr IN TICKET.TC_TICKET_NUMBER%TYPE,
  p_acc_id IN TICKET.TC_ACCOUNT_ID%TYPE,
  p_text IN TICKET.TC_TEXT%TYPE,
  p_state IN TICKET.TC_STATE%TYPE,
  p_device IN TICKET.TC_DEVICE_NAME%TYPE)
IS
BEGIN
  INSERT INTO TICKET
  VALUES(p_nr, p_acc_id, p_text, SYSDATE, p_state, p_device);
  COMMIT;
END;