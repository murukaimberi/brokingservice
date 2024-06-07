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

describe('SubClassOfBusiness e2e test', () => {
  const subClassOfBusinessPageUrl = '/sub-class-of-business';
  const subClassOfBusinessPageUrlPattern = new RegExp('/sub-class-of-business(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const subClassOfBusinessSample = {};

  let subClassOfBusiness;
  // let classOfBusiness;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/class-of-businesses',
      body: {"name":"unabashedly"},
    }).then(({ body }) => {
      classOfBusiness = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/sub-class-of-businesses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/sub-class-of-businesses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/sub-class-of-businesses/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/contracts', {
      statusCode: 200,
      body: [],
    });

    cy.intercept('GET', '/api/class-of-businesses', {
      statusCode: 200,
      body: [classOfBusiness],
    });

  });
   */

  afterEach(() => {
    if (subClassOfBusiness) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sub-class-of-businesses/${subClassOfBusiness.id}`,
      }).then(() => {
        subClassOfBusiness = undefined;
      });
    }
  });

  /* Disabled due to incompatibility
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
   */

  it('SubClassOfBusinesses menu should load SubClassOfBusinesses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('sub-class-of-business');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('SubClassOfBusiness').should('exist');
    cy.url().should('match', subClassOfBusinessPageUrlPattern);
  });

  describe('SubClassOfBusiness page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(subClassOfBusinessPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create SubClassOfBusiness page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/sub-class-of-business/new$'));
        cy.getEntityCreateUpdateHeading('SubClassOfBusiness');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subClassOfBusinessPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/sub-class-of-businesses',
          body: {
            ...subClassOfBusinessSample,
            classOfBusiness: classOfBusiness,
          },
        }).then(({ body }) => {
          subClassOfBusiness = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/sub-class-of-businesses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/sub-class-of-businesses?page=0&size=20>; rel="last",<http://localhost/api/sub-class-of-businesses?page=0&size=20>; rel="first"',
              },
              body: [subClassOfBusiness],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(subClassOfBusinessPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(subClassOfBusinessPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details SubClassOfBusiness page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('subClassOfBusiness');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subClassOfBusinessPageUrlPattern);
      });

      it('edit button click should load edit SubClassOfBusiness page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubClassOfBusiness');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subClassOfBusinessPageUrlPattern);
      });

      it('edit button click should load edit SubClassOfBusiness page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('SubClassOfBusiness');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subClassOfBusinessPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of SubClassOfBusiness', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('subClassOfBusiness').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', subClassOfBusinessPageUrlPattern);

        subClassOfBusiness = undefined;
      });
    });
  });

  describe('new SubClassOfBusiness page', () => {
    beforeEach(() => {
      cy.visit(`${subClassOfBusinessPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('SubClassOfBusiness');
    });

    it.skip('should create an instance of SubClassOfBusiness', () => {
      cy.get(`[data-cy="name"]`).type('ouch mandate');
      cy.get(`[data-cy="name"]`).should('have.value', 'ouch mandate');

      cy.get(`[data-cy="classOfBusiness"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        subClassOfBusiness = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', subClassOfBusinessPageUrlPattern);
    });
  });
});
