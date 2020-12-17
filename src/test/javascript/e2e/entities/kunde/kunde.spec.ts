import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { KundeComponentsPage, KundeDeleteDialog, KundeUpdatePage } from './kunde.page-object';

const expect = chai.expect;

describe('Kunde e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let kundeComponentsPage: KundeComponentsPage;
  let kundeUpdatePage: KundeUpdatePage;
  let kundeDeleteDialog: KundeDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Kundes', async () => {
    await navBarPage.goToEntity('kunde');
    kundeComponentsPage = new KundeComponentsPage();
    await browser.wait(ec.visibilityOf(kundeComponentsPage.title), 5000);
    expect(await kundeComponentsPage.getTitle()).to.eq('kundenportalApp.kunde.home.title');
    await browser.wait(ec.or(ec.visibilityOf(kundeComponentsPage.entities), ec.visibilityOf(kundeComponentsPage.noResult)), 1000);
  });

  it('should load create Kunde page', async () => {
    await kundeComponentsPage.clickOnCreateButton();
    kundeUpdatePage = new KundeUpdatePage();
    expect(await kundeUpdatePage.getPageTitle()).to.eq('kundenportalApp.kunde.home.createOrEditLabel');
    await kundeUpdatePage.cancel();
  });

  it('should create and save Kundes', async () => {
    const nbButtonsBeforeCreate = await kundeComponentsPage.countDeleteButtons();

    await kundeComponentsPage.clickOnCreateButton();

    await promise.all([
      kundeUpdatePage.anredeSelectLastOption(),
      kundeUpdatePage.setNameInput('name'),
      kundeUpdatePage.setVornameInput('vorname'),
      kundeUpdatePage.setEmailInput('email'),
      kundeUpdatePage.setStrasseInput('strasse'),
      kundeUpdatePage.setPlzortInput('plzort'),
      kundeUpdatePage.setTelefonnrInput('telefonnr'),
      kundeUpdatePage.setIbanInput('iban'),
      kundeUpdatePage.setCreatedInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      kundeUpdatePage.userSelectLastOption(),
    ]);

    expect(await kundeUpdatePage.getNameInput()).to.eq('name', 'Expected Name value to be equals to name');
    expect(await kundeUpdatePage.getVornameInput()).to.eq('vorname', 'Expected Vorname value to be equals to vorname');
    expect(await kundeUpdatePage.getEmailInput()).to.eq('email', 'Expected Email value to be equals to email');
    expect(await kundeUpdatePage.getStrasseInput()).to.eq('strasse', 'Expected Strasse value to be equals to strasse');
    expect(await kundeUpdatePage.getPlzortInput()).to.eq('plzort', 'Expected Plzort value to be equals to plzort');
    expect(await kundeUpdatePage.getTelefonnrInput()).to.eq('telefonnr', 'Expected Telefonnr value to be equals to telefonnr');
    expect(await kundeUpdatePage.getIbanInput()).to.eq('iban', 'Expected Iban value to be equals to iban');
    expect(await kundeUpdatePage.getCreatedInput()).to.contain('2001-01-01T02:30', 'Expected created value to be equals to 2000-12-31');

    await kundeUpdatePage.save();
    expect(await kundeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await kundeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Kunde', async () => {
    const nbButtonsBeforeDelete = await kundeComponentsPage.countDeleteButtons();
    await kundeComponentsPage.clickOnLastDeleteButton();

    kundeDeleteDialog = new KundeDeleteDialog();
    expect(await kundeDeleteDialog.getDialogTitle()).to.eq('kundenportalApp.kunde.delete.question');
    await kundeDeleteDialog.clickOnConfirmButton();

    expect(await kundeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
