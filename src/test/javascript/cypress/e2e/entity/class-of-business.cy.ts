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

describe('ClassOfBusiness e2e test', () => {
  const classOfBusinessPageUrl = '/class-of-business';
  const classOfBusinessPageUrlPattern = new RegExp('/class-of-business(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const classOfBusinessSample = {};

  let classOfBusiness;
  let insuranceType;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/insurance-types',
      body: { name: 'phew yum embody' },
    }).then(({ body }) => {
      insuranceType = body;
    });
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/class-of-businesses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/class-of-businesses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/class-of-businesses/*').as('deleteEntityRequest');
  });

  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/sub-class-of-businesses', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/contracts', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/insurance-types', {
      statusCode: 200,
      body: [insuranceType],
    });
  });

  afterEach(() => {
    if (classOfBusiness) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/class-of-businesses/${classOfBusiness.id}`,
      }).then(() => {
        classOfBusiness = undefined;
      });
    }
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

  it('ClassOfBusinesses menu should load ClassOfBusinesses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('class-of-business');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ClassOfBusiness').should('exist');
    cy.url().should('match', classOfBusinessPageUrlPattern);
  });

  describe('ClassOfBusiness page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(classOfBusinessPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ClassOfBusiness page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/class-of-business/new$'));
        cy.getEntityCreateUpdateHeading('ClassOfBusiness');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', classOfBusinessPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/class-of-businesses',
          body: {
            ...classOfBusinessSample,
            insuranceType: insuranceType,
          },
        }).then(({ body }) => {
          classOfBusiness = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/class-of-businesses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/class-of-businesses?page=0&size=20>; rel="last",<http://localhost/api/class-of-businesses?page=0&size=20>; rel="first"',
              },
              body: [classOfBusiness],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(classOfBusinessPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ClassOfBusiness page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('classOfBusiness');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', classOfBusinessPageUrlPattern);
      });

      it('edit button click should load edit ClassOfBusiness page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ClassOfBusiness');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', classOfBusinessPageUrlPattern);
      });

      it('edit button click should load edit ClassOfBusiness page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ClassOfBusiness');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', classOfBusinessPageUrlPattern);
      });

      it('last delete button click should delete instance of ClassOfBusiness', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('classOfBusiness').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', classOfBusinessPageUrlPattern);

        classOfBusiness = undefined;
      });
    });
  });

  describe('new ClassOfBusiness page', () => {
    beforeEach(() => {
      cy.visit(`${classOfBusinessPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ClassOfBusiness');
    });

    it('should create an instance of ClassOfBusiness', () => {
      cy.get(`[data-cy="name"]`).type('eek');
      cy.get(`[data-cy="name"]`).should('have.value', 'eek');

      cy.get(`[data-cy="insuranceType"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        classOfBusiness = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', classOfBusinessPageUrlPattern);
    });
  });
});
