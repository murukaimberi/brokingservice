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

describe('Address e2e test', () => {
  const addressPageUrl = '/address';
  const addressPageUrlPattern = new RegExp('/address(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const addressSample = {
    streetAddress: 'Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=',
    city: 'Collierland',
    province: 'scorch',
    zipCode: '33921-8520',
    country: 'Belize',
  };

  let address;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/addresses+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/addresses').as('postEntityRequest');
    cy.intercept('DELETE', '/api/addresses/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (address) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/addresses/${address.id}`,
      }).then(() => {
        address = undefined;
      });
    }
  });

  it('Addresses menu should load Addresses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('address');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Address').should('exist');
    cy.url().should('match', addressPageUrlPattern);
  });

  describe('Address page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(addressPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Address page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/address/new$'));
        cy.getEntityCreateUpdateHeading('Address');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', addressPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/addresses',
          body: addressSample,
        }).then(({ body }) => {
          address = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/addresses+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/addresses?page=0&size=20>; rel="last",<http://localhost/api/addresses?page=0&size=20>; rel="first"',
              },
              body: [address],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(addressPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Address page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('address');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', addressPageUrlPattern);
      });

      it('edit button click should load edit Address page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Address');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', addressPageUrlPattern);
      });

      it('edit button click should load edit Address page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Address');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', addressPageUrlPattern);
      });

      it('last delete button click should delete instance of Address', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('address').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', addressPageUrlPattern);

        address = undefined;
      });
    });
  });

  describe('new Address page', () => {
    beforeEach(() => {
      cy.visit(`${addressPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Address');
    });

    it('should create an instance of Address', () => {
      cy.get(`[data-cy="streetAddress"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="streetAddress"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="city"]`).type('Apex');
      cy.get(`[data-cy="city"]`).should('have.value', 'Apex');

      cy.get(`[data-cy="province"]`).type('separately although diligently');
      cy.get(`[data-cy="province"]`).should('have.value', 'separately although diligently');

      cy.get(`[data-cy="state"]`).type('yowza freight geez');
      cy.get(`[data-cy="state"]`).should('have.value', 'yowza freight geez');

      cy.get(`[data-cy="zipCode"]`).type('94890-8182');
      cy.get(`[data-cy="zipCode"]`).should('have.value', '94890-8182');

      cy.get(`[data-cy="country"]`).type('Dominica');
      cy.get(`[data-cy="country"]`).should('have.value', 'Dominica');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        address = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', addressPageUrlPattern);
    });
  });
});
