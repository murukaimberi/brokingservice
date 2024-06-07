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

describe('Contract e2e test', () => {
  const contractPageUrl = '/contract';
  const contractPageUrlPattern = new RegExp('/contract(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  // const contractSample = {"type":"TREATY","subType":"NON_PROPORTIONAL","inception":"2024-06-07T00:11:07.897Z","expiry":"2024-06-07T06:18:48.861Z","currency":"gee intellect along","totalSumInsured":1139.47,"limitOfLiability":3392.36,"status":"UPDATE_CREATED"};

  let contract;
  // let businessPartner;
  // let classOfBusiness;
  // let subClassOfBusiness;
  // let country;

  beforeEach(() => {
    cy.login(username, password);
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/business-partners',
      body: {"name":"distract","description":"Li4vZmFrZS1kYXRhL2Jsb2IvaGlwc3Rlci50eHQ=","representativeName":"grandmom meaningfully","email":"Haylee_Kreiger@yahoo.com","phoneNumber":"that","agentType":"BROKER"},
    }).then(({ body }) => {
      businessPartner = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/class-of-businesses',
      body: {"name":"where or amidst"},
    }).then(({ body }) => {
      classOfBusiness = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/sub-class-of-businesses',
      body: {"name":"quickly"},
    }).then(({ body }) => {
      subClassOfBusiness = body;
    });
    // create an instance at the required relationship entity:
    cy.authenticatedRequest({
      method: 'POST',
      url: '/api/countries',
      body: {"name":"geez failing","currency":"globalise good sheepishly","currencyCode":"PHP"},
    }).then(({ body }) => {
      country = body;
    });
  });
   */

  beforeEach(() => {
    cy.intercept('GET', '/api/contracts+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/contracts').as('postEntityRequest');
    cy.intercept('DELETE', '/api/contracts/*').as('deleteEntityRequest');
  });

  /* Disabled due to incompatibility
  beforeEach(() => {
    // Simulate relationships api for better performance and reproducibility.
    cy.intercept('GET', '/api/business-partners', {
      statusCode: 200,
      body: [businessPartner],
    });

    cy.intercept('GET', '/api/class-of-businesses', {
      statusCode: 200,
      body: [classOfBusiness],
    });

    cy.intercept('GET', '/api/sub-class-of-businesses', {
      statusCode: 200,
      body: [subClassOfBusiness],
    });

    cy.intercept('GET', '/api/countries', {
      statusCode: 200,
      body: [country],
    });

    cy.intercept('GET', '/api/reinsurance-placements', {
      statusCode: 200,
      body: [],
    });

  });
   */

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

  /* Disabled due to incompatibility
  afterEach(() => {
    if (businessPartner) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/business-partners/${businessPartner.id}`,
      }).then(() => {
        businessPartner = undefined;
      });
    }
    if (classOfBusiness) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/class-of-businesses/${classOfBusiness.id}`,
      }).then(() => {
        classOfBusiness = undefined;
      });
    }
    if (subClassOfBusiness) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/sub-class-of-businesses/${subClassOfBusiness.id}`,
      }).then(() => {
        subClassOfBusiness = undefined;
      });
    }
    if (country) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/countries/${country.id}`,
      }).then(() => {
        country = undefined;
      });
    }
  });
   */

  it('Contracts menu should load Contracts page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('contract');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response?.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Contract').should('exist');
    cy.url().should('match', contractPageUrlPattern);
  });

  describe('Contract page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(contractPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Contract page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/contract/new$'));
        cy.getEntityCreateUpdateHeading('Contract');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contractPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      /* Disabled due to incompatibility
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/contracts',
          body: {
            ...contractSample,
            insured: businessPartner,
            insurer: businessPartner,
            broker: businessPartner,
            reinsurers: businessPartner,
            classOfBusiness: classOfBusiness,
            subClassOfBusiness: subClassOfBusiness,
            country: country,
          },
        }).then(({ body }) => {
          contract = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/contracts+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/contracts?page=0&size=20>; rel="last",<http://localhost/api/contracts?page=0&size=20>; rel="first"',
              },
              body: [contract],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(contractPageUrl);

        cy.wait('@entitiesRequestInternal');
      });
       */

      beforeEach(function () {
        cy.visit(contractPageUrl);

        cy.wait('@entitiesRequest').then(({ response }) => {
          if (response?.body.length === 0) {
            this.skip();
          }
        });
      });

      it('detail button click should load details Contract page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('contract');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contractPageUrlPattern);
      });

      it('edit button click should load edit Contract page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Contract');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contractPageUrlPattern);
      });

      it('edit button click should load edit Contract page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Contract');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contractPageUrlPattern);
      });

      it.skip('last delete button click should delete instance of Contract', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('contract').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response?.statusCode).to.equal(200);
        });
        cy.url().should('match', contractPageUrlPattern);

        contract = undefined;
      });
    });
  });

  describe('new Contract page', () => {
    beforeEach(() => {
      cy.visit(`${contractPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Contract');
    });

    it.skip('should create an instance of Contract', () => {
      cy.get(`[data-cy="type"]`).select('FACULTATIVE');

      cy.get(`[data-cy="subType"]`).select('NON_PROPORTIONAL');

      cy.get(`[data-cy="inception"]`).type('2024-06-07T11:48');
      cy.get(`[data-cy="inception"]`).blur();
      cy.get(`[data-cy="inception"]`).should('have.value', '2024-06-07T11:48');

      cy.get(`[data-cy="expiry"]`).type('2024-06-06T16:08');
      cy.get(`[data-cy="expiry"]`).blur();
      cy.get(`[data-cy="expiry"]`).should('have.value', '2024-06-06T16:08');

      cy.get(`[data-cy="currency"]`).type('yahoo patiently');
      cy.get(`[data-cy="currency"]`).should('have.value', 'yahoo patiently');

      cy.get(`[data-cy="totalSumInsured"]`).type('16979.92');
      cy.get(`[data-cy="totalSumInsured"]`).should('have.value', '16979.92');

      cy.get(`[data-cy="limitOfLiability"]`).type('5559.7');
      cy.get(`[data-cy="limitOfLiability"]`).should('have.value', '5559.7');

      cy.get(`[data-cy="uuid"]`).type('1cf04287-f9bb-4abd-8638-2bee2fa0cb46');
      cy.get(`[data-cy="uuid"]`).invoke('val').should('match', new RegExp('1cf04287-f9bb-4abd-8638-2bee2fa0cb46'));

      cy.get(`[data-cy="status"]`).select('CREATED_SHARE_FINALISED');

      cy.get(`[data-cy="active"]`).should('not.be.checked');
      cy.get(`[data-cy="active"]`).click();
      cy.get(`[data-cy="active"]`).should('be.checked');

      cy.get(`[data-cy="insured"]`).select(1);
      cy.get(`[data-cy="insurer"]`).select(1);
      cy.get(`[data-cy="broker"]`).select(1);
      cy.get(`[data-cy="reinsurers"]`).select([0]);
      cy.get(`[data-cy="classOfBusiness"]`).select(1);
      cy.get(`[data-cy="subClassOfBusiness"]`).select(1);
      cy.get(`[data-cy="country"]`).select(1);

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(201);
        contract = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response?.statusCode).to.equal(200);
      });
      cy.url().should('match', contractPageUrlPattern);
    });
  });
});
