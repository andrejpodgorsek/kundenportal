import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { AgenturComponentsPage, AgenturDeleteDialog, AgenturUpdatePage } from './agentur.page-object';

const expect = chai.expect;

describe('Agentur e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let agenturComponentsPage: AgenturComponentsPage;
  let agenturUpdatePage: AgenturUpdatePage;
  let agenturDeleteDialog: AgenturDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Agenturs', async () => {
    await navBarPage.goToEntity('agentur');
    agenturComponentsPage = new AgenturComponentsPage();
    await browser.wait(ec.visibilityOf(agenturComponentsPage.title), 5000);
    expect(await agenturComponentsPage.getTitle()).to.eq('kundenportalApp.agentur.home.title');
    await browser.wait(ec.or(ec.visibilityOf(agenturComponentsPage.entities), ec.visibilityOf(agenturComponentsPage.noResult)), 1000);
  });

  it('should load create Agentur page', async () => {
    await agenturComponentsPage.clickOnCreateButton();
    agenturUpdatePage = new AgenturUpdatePage();
    expect(await agenturUpdatePage.getPageTitle()).to.eq('kundenportalApp.agentur.home.createOrEditLabel');
    await agenturUpdatePage.cancel();
  });

  it('should create and save Agenturs', async () => {
    const nbButtonsBeforeCreate = await agenturComponentsPage.countDeleteButtons();

    await agenturComponentsPage.clickOnCreateButton();

    await promise.all([
      agenturUpdatePage.setAgenturnummerInput('agenturnummer'),
      agenturUpdatePage.setNameInput('name'),
      agenturUpdatePage.setAdresseInput('adresse'),
      agenturUpdatePage.setEmailInput('email'),
      agenturUpdatePage.setTelefonnrInput('telefonnr'),
      agenturUpdatePage.setCreatedInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      agenturUpdatePage.userSelectLastOption(),
    ]);

    expect(await agenturUpdatePage.getAgenturnummerInput()).to.eq(
      'agenturnummer',
      'Expected Agenturnummer value to be equals to agenturnummer'
    );
    expect(await agenturUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await agenturUpdatePage.getAdresseInput()).to.eq('adresse', 'Expected Adresse value to be equals to adresse');
    expect(await agenturUpdatePage.getEmailInput()).to.eq('email', 'Expected Email value to be equals to email');
    expect(await agenturUpdatePage.getTelefonnrInput()).to.eq('telefonnr', 'Expected Telefonnr value to be equals to telefonnr');
    expect(await agenturUpdatePage.getCreatedInput()).to.contain('2001-01-01T02:30', 'Expected created value to be equals to 2000-12-31');

    await agenturUpdatePage.save();
    expect(await agenturUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await agenturComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Agentur', async () => {
    const nbButtonsBeforeDelete = await agenturComponentsPage.countDeleteButtons();
    await agenturComponentsPage.clickOnLastDeleteButton();

    agenturDeleteDialog = new AgenturDeleteDialog();
    expect(await agenturDeleteDialog.getDialogTitle()).to.eq('kundenportalApp.agentur.delete.question');
    await agenturDeleteDialog.clickOnConfirmButton();

    expect(await agenturComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
