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

describe('ReinsurancePlacement e2e test', () => {
  const reinsurancePlacementPageUrl = '/reinsurance-placement';
  const reinsurancePlacementPageUrlPattern = new RegExp('/reinsurance-placement(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const reinsurancePlacementSample = {"riPercentageCommission":36.93,"riPercentageShare":1.25,"grossPremiumHundred":13873.86};

  let reinsurancePlacement;
  // let contract;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/contracts',
      body: {"type":"FACULTATIVE","subType":"NON_PROPORTIONAL","inception":"2024-06-06T18:01:10.835Z","expiry":"2024-06-06T13:22:48.127Z","currency":"aside ratify wrong","totalSumInsured":13680.4,"limitOfLiability":13044.03,"uuid":"048d1682-52fb-448d-8180-478d71d954b2","status":"CREATED_APPROVED","active":false},
    }).then(({ body }) => {
      contract = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/reinsurance-placements+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/reinsurance-placements').as('postEntityRequest');
    cy.intercept('DELETE', '/api/reinsurance-placements/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/contracts', {
      statusCode: 200,
      body: [contract],
    });

  });
   */

  afterEach(() => {
    if (reinsurancePlacement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/reinsurance-placements/${reinsurancePlacement.id}`,
      }).then(() => {
        reinsurancePlacement = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
  afterEach(() => {
    if (contract) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/contracts/${contract.id}`,
      }).then(() => {
        contract = undefined;
      });
    }
  });
   */

  it('ReinsurancePlacements menu should load ReinsurancePlacements page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('reinsurance-placement');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ReinsurancePlacement').should('exist');
    cy.url().should('match', reinsurancePlacementPageUrlPattern);
  });

  describe('ReinsurancePlacement page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(reinsurancePlacementPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ReinsurancePlacement page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/reinsurance-placement/new$'));
        cy.getEntityCreateUpdateHeading('ReinsurancePlacement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', reinsurancePlacementPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/reinsurance-placements',
          body: {
            ...reinsurancePlacementSample,
            contract: contract,
          },
        }).then(({ body }) => {
          reinsurancePlacement = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/reinsurance-placements+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/reinsurance-placements?page=0&size=20>; rel="last",<http://localhost/api/reinsurance-placements?page=0&size=20>; rel="first"',
              },
              body: [reinsurancePlacement],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(reinsurancePlacementPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(reinsurancePlacementPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details ReinsurancePlacement page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('reinsurancePlacement');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', reinsurancePlacementPageUrlPattern);
      });

      it('edit button click should load edit ReinsurancePlacement page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReinsurancePlacement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', reinsurancePlacementPageUrlPattern);
      });

      it('edit button click should load edit ReinsurancePlacement page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ReinsurancePlacement');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', reinsurancePlacementPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of ReinsurancePlacement', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('reinsurancePlacement').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', reinsurancePlacementPageUrlPattern);

        reinsurancePlacement = undefined;
      });
    });
  });

  describe('new ReinsurancePlacement page', () => {
    beforeEach(() => {
      cy.visit(`${reinsurancePlacementPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ReinsurancePlacement');
    });

    it.skip('should create an instance of ReinsurancePlacement', () => {
      cy.get(`[data-cy="riPercentageCommission"]`).type('15.46');
      cy.get(`[data-cy="riPercentageCommission"]`).should('have.value', '15.46');

      cy.get(`[data-cy="riPercentageShare"]`).type('34.17');
      cy.get(`[data-cy="riPercentageShare"]`).should('have.value', '34.17');

      cy.get(`[data-cy="riTotalSumInsured"]`).type('18226.64');
      cy.get(`[data-cy="riTotalSumInsured"]`).should('have.value', '18226.64');

      cy.get(`[data-cy="riLimitOfLiability"]`).type('13416.77');
      cy.get(`[data-cy="riLimitOfLiability"]`).should('have.value', '13416.77');

      cy.get(`[data-cy="grossPremiumHundred"]`).type('14270.2');
      cy.get(`[data-cy="grossPremiumHundred"]`).should('have.value', '14270.2');

      cy.get(`[data-cy="riPremium"]`).type('7570.09');
      cy.get(`[data-cy="riPremium"]`).should('have.value', '7570.09');

      cy.get(`[data-cy="riCommission"]`).type('7845.23');
      cy.get(`[data-cy="riCommission"]`).should('have.value', '7845.23');

      cy.get(`[data-cy="netDueFromInsurer"]`).type('14962.42');
      cy.get(`[data-cy="netDueFromInsurer"]`).should('have.value', '14962.42');

      cy.get(`[data-cy="contract"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        reinsurancePlacement = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', reinsurancePlacementPageUrlPattern);
    });
  });
});
