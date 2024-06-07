import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('ContractHistory e2e test', () => {
  const contractHistoryPageUrl = '/contract-history';
  const contractHistoryPageUrlPattern = new RegExp('/contract-history(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const contractHistorySample = {"contractCreatedDate":"2024-06-06T12:42:12.518Z","contractActiveDate":"2024-06-06T18:35:00.755Z","changeDescription":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ="};

  let contractHistory;
  // let user;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/users',
      body: {"login":"-O","firstName":"Fidel","lastName":"Stamm","email":"Gerard_Mills84@yahoo.com","imageUrl":"instead","langKey":"battleship"},
    }).then(({ body }) => {
      user = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/contract-histories+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contract-histories').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contract-histories/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/users', {
      statusCode: 200,
      body: [user],
    });

  });
   */

  afterEach(() => {
    if (contractHistory) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/contract-histories/${contractHistory.id}`,
      }).then(() => {
        contractHistory = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (user) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/users/${user.id}`,
      }).then(() => {
        user = undefined;
      });
    }
  });
   */

  it('ContractHistories menu should load ContractHistories page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('contract-history');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ContractHistory').should('exist');
    cy.url().should('match', contractHistoryPageUrlPattern);
  });

  describe('ContractHistory page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(contractHistoryPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ContractHistory page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/contract-history/new$'));
        cy.getEntityCreateUpdateHeading('ContractHistory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contractHistoryPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/contract-histories',
          body: {
            ...contractHistorySample,
            updated: user,
          },
        }).then(({ body }) => {
          contractHistory = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/contract-histories+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/contract-histories?page=0&size=20>; rel="last",<http://localhost/api/contract-histories?page=0&size=20>; rel="first"',
              },
              body: [contractHistory],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(contractHistoryPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(contractHistoryPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ContractHistory page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('contractHistory');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contractHistoryPageUrlPattern);
      });

      it('edit button click should load edit ContractHistory page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContractHistory');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contractHistoryPageUrlPattern);
      });

      it('edit button click should load edit ContractHistory page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContractHistory');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contractHistoryPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of ContractHistory', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('contractHistory').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contractHistoryPageUrlPattern);

        contractHistory = undefined;
      });
    });
  });

  describe('new ContractHistory page', () => {
    beforeEach(() => {
      cy.visit(`${contractHistoryPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ContractHistory');
    });

    it.skip('should create an instance of ContractHistory', () => {
      cy.get(`[data-cy="contractCreatedDate"]`).type('2024-06-07T10:35');
      cy.get(`[data-cy="contractCreatedDate"]`).blur();
      cy.get(`[data-cy="contractCreatedDate"]`).should('have.value', '2024-06-07T10:35');

      cy.get(`[data-cy="contractActiveDate"]`).type('2024-06-07T10:10');
      cy.get(`[data-cy="contractActiveDate"]`).blur();
      cy.get(`[data-cy="contractActiveDate"]`).should('have.value', '2024-06-07T10:10');

      cy.get(`[data-cy="contractInActiveDate"]`).type('2024-06-06T23:46');
      cy.get(`[data-cy="contractInActiveDate"]`).blur();
      cy.get(`[data-cy="contractInActiveDate"]`).should('have.value', '2024-06-06T23:46');

      cy.get(`[data-cy="contractLastModifiedDate"]`).type('2024-06-07T01:57');
      cy.get(`[data-cy="contractLastModifiedDate"]`).blur();
      cy.get(`[data-cy="contractLastModifiedDate"]`).should('have.value', '2024-06-07T01:57');

      cy.get(`[data-cy="changeDescription"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="changeDescription"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="updated"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        contractHistory = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', contractHistoryPageUrlPattern);
    });
  });
});
