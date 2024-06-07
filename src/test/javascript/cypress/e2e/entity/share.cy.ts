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

describe('Share e2e test', () => {
  const sharePageUrl = '/share';
  const sharePageUrlPattern = new RegExp('/share(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const shareSample = { sharePercentage: 26.55, brokerage: 77.9 };

  let share;
  let businessPartner;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/business-partners',
      body: {
        name: 'benefit knowingly',
        description: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
        representativeName: 'yippee viciously athwart',
        email: 'Dakota.Zemlak92@gmail.com',
        phoneNumber: 'nor',
        agentType: 'INSURER',
      },
    }).then(({ body }) => {
      businessPartner = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/shares+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/shares').as('postEntityRequest');
    cy.intercept('DELETE', '/api/shares/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/business-partners', {
      statusCode: 200,
      body: [businessPartner],
    });
  });

  afterEach(() => {
    if (share) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shares/${share.id}`,
      }).then(() => {
        share = undefined;
      });
    }
  });

  afterEach(() => {
    if (businessPartner) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/business-partners/${businessPartner.id}`,
      }).then(() => {
        businessPartner = undefined;
      });
    }
  });

  it('Shares menu should load Shares page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('share');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Share').should('exist');
    cy.url().should('match', sharePageUrlPattern);
  });

  describe('Share page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(sharePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Share page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/share/new$'));
        cy.getEntityCreateUpdateHeading('Share');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sharePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/shares',
          body: {
            ...shareSample,
            reInsurer: businessPartner,
          },
        }).then(({ body }) => {
          share = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/shares+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/shares?page=0&size=20>; rel="last",<http://localhost/api/shares?page=0&size=20>; rel="first"',
              },
              body: [share],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(sharePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Share page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('share');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sharePageUrlPattern);
      });

      it('edit button click should load edit Share page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Share');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sharePageUrlPattern);
      });

      it('edit button click should load edit Share page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Share');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sharePageUrlPattern);
      });

      it('last delete button click should delete instance of Share', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('share').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', sharePageUrlPattern);

        share = undefined;
      });
    });
  });

  describe('new Share page', () => {
    beforeEach(() => {
      cy.visit(`${sharePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Share');
    });

    it('should create an instance of Share', () => {
      cy.get(`[data-cy="sharePercentage"]`).type('25');
      cy.get(`[data-cy="sharePercentage"]`).should('have.value', '25');

      cy.get(`[data-cy="totalSumInsured"]`).type('18907.79');
      cy.get(`[data-cy="totalSumInsured"]`).should('have.value', '18907.79');

      cy.get(`[data-cy="limitOfLiability"]`).type('22902.29');
      cy.get(`[data-cy="limitOfLiability"]`).should('have.value', '22902.29');

      cy.get(`[data-cy="grossPremium"]`).type('27001.12');
      cy.get(`[data-cy="grossPremium"]`).should('have.value', '27001.12');

      cy.get(`[data-cy="riCommission"]`).type('6206.65');
      cy.get(`[data-cy="riCommission"]`).should('have.value', '6206.65');

      cy.get(`[data-cy="netPremium"]`).type('9635.64');
      cy.get(`[data-cy="netPremium"]`).should('have.value', '9635.64');

      cy.get(`[data-cy="brokerage"]`).type('15.78');
      cy.get(`[data-cy="brokerage"]`).should('have.value', '15.78');

      cy.get(`[data-cy="brokerageAmount"]`).type('744.39');
      cy.get(`[data-cy="brokerageAmount"]`).should('have.value', '744.39');

      cy.get(`[data-cy="netPayable"]`).type('22857.89');
      cy.get(`[data-cy="netPayable"]`).should('have.value', '22857.89');

      cy.get(`[data-cy="reInsurer"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        share = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', sharePageUrlPattern);
    });
  });
});
