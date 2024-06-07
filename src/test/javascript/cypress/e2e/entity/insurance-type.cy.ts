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

describe('InsuranceType e2e test', () => {
  const insuranceTypePageUrl = '/insurance-type';
  const insuranceTypePageUrlPattern = new RegExp('/insurance-type(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const insuranceTypeSample = { name: 'aha faithfully' };

  let insuranceType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/insurance-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/insurance-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/insurance-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (insuranceType) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/insurance-types/${insuranceType.id}`,
      }).then(() => {
        insuranceType = undefined;
      });
    }
  });

  it('InsuranceTypes menu should load InsuranceTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('insurance-type');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('InsuranceType').should('exist');
    cy.url().should('match', insuranceTypePageUrlPattern);
  });

  describe('InsuranceType page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(insuranceTypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create InsuranceType page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/insurance-type/new$'));
        cy.getEntityCreateUpdateHeading('InsuranceType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', insuranceTypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/insurance-types',
          body: insuranceTypeSample,
        }).then(({ body }) => {
          insuranceType = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/insurance-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/insurance-types?page=0&size=20>; rel="last",<http://localhost/api/insurance-types?page=0&size=20>; rel="first"',
              },
              body: [insuranceType],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(insuranceTypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details InsuranceType page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('insuranceType');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', insuranceTypePageUrlPattern);
      });

      it('edit button click should load edit InsuranceType page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InsuranceType');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', insuranceTypePageUrlPattern);
      });

      it('edit button click should load edit InsuranceType page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('InsuranceType');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', insuranceTypePageUrlPattern);
      });

      it('last delete button click should delete instance of InsuranceType', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('insuranceType').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', insuranceTypePageUrlPattern);

        insuranceType = undefined;
      });
    });
  });

  describe('new InsuranceType page', () => {
    beforeEach(() => {
      cy.visit(`${insuranceTypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('InsuranceType');
    });

    it('should create an instance of InsuranceType', () => {
      cy.get(`[data-cy="name"]`).type('kindly');
      cy.get(`[data-cy="name"]`).should('have.value', 'kindly');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        insuranceType = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', insuranceTypePageUrlPattern);
    });
  });
});
