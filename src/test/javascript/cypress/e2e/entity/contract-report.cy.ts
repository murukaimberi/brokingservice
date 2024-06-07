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

describe('ContractReport e2e test', () => {
  const contractReportPageUrl = '/contract-report';
  const contractReportPageUrlPattern = new RegExp('/contract-report(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const contractReportSample = {
    contractDocument: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci5wbmc=',
    contractDocumentContentType: 'unknown',
    createdDate: '2024-06-06T14:40:22.396Z',
  };

  let contractReport;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/contract-reports+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contract-reports').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contract-reports/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (contractReport) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/contract-reports/${contractReport.id}`,
      }).then(() => {
        contractReport = undefined;
      });
    }
  });

  it('ContractReports menu should load ContractReports page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('contract-report');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ContractReport').should('exist');
    cy.url().should('match', contractReportPageUrlPattern);
  });

  describe('ContractReport page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(contractReportPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ContractReport page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/contract-report/new$'));
        cy.getEntityCreateUpdateHeading('ContractReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contractReportPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/contract-reports',
          body: contractReportSample,
        }).then(({ body }) => {
          contractReport = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/contract-reports+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/contract-reports?page=0&size=20>; rel="last",<http://localhost/api/contract-reports?page=0&size=20>; rel="first"',
              },
              body: [contractReport],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(contractReportPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ContractReport page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('contractReport');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contractReportPageUrlPattern);
      });

      it('edit button click should load edit ContractReport page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContractReport');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contractReportPageUrlPattern);
      });

      it('edit button click should load edit ContractReport page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ContractReport');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contractReportPageUrlPattern);
      });

      it('last delete button click should delete instance of ContractReport', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('contractReport').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contractReportPageUrlPattern);

        contractReport = undefined;
      });
    });
  });

  describe('new ContractReport page', () => {
    beforeEach(() => {
      cy.visit(`${contractReportPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ContractReport');
    });

    it('should create an instance of ContractReport', () => {
      cy.setFieldImageAsBytesOfEntity('contractDocument', 'integration-test.png', 'image/png');

      cy.get(`[data-cy="createdDate"]`).type('2024-06-07T04:45');
      cy.get(`[data-cy="createdDate"]`).blur();
      cy.get(`[data-cy="createdDate"]`).should('have.value', '2024-06-07T04:45');

      // since cypress clicks submit too fast before the blob fields are validated
      cy.wait(200); // eslint-disable-line cypress/no-unnecessary-waiting
      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        contractReport = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', contractReportPageUrlPattern);
    });
  });
});
