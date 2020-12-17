import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { DokumentComponentsPage, DokumentDeleteDialog, DokumentUpdatePage } from './dokument.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Dokument e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let dokumentComponentsPage: DokumentComponentsPage;
  let dokumentUpdatePage: DokumentUpdatePage;
  let dokumentDeleteDialog: DokumentDeleteDialog;
  const fileNameToUpload = 'logo-jhipster.png';
  const fileToUpload = '../../../../../../src/main/webapp/content/images/' + fileNameToUpload;
  const absolutePath = path.resolve(__dirname, fileToUpload);

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Dokuments', async () => {
    await navBarPage.goToEntity('dokument');
    dokumentComponentsPage = new DokumentComponentsPage();
    await browser.wait(ec.visibilityOf(dokumentComponentsPage.title), 5000);
    expect(await dokumentComponentsPage.getTitle()).to.eq('kundenportalApp.dokument.home.title');
    await browser.wait(ec.or(ec.visibilityOf(dokumentComponentsPage.entities), ec.visibilityOf(dokumentComponentsPage.noResult)), 1000);
  });

  it('should load create Dokument page', async () => {
    await dokumentComponentsPage.clickOnCreateButton();
    dokumentUpdatePage = new DokumentUpdatePage();
    expect(await dokumentUpdatePage.getPageTitle()).to.eq('kundenportalApp.dokument.home.createOrEditLabel');
    await dokumentUpdatePage.cancel();
  });

  it('should create and save Dokuments', async () => {
    const nbButtonsBeforeCreate = await dokumentComponentsPage.countDeleteButtons();

    await dokumentComponentsPage.clickOnCreateButton();

    await promise.all([
      dokumentUpdatePage.setNummerInput('nummer'),
      dokumentUpdatePage.dokumentSelectLastOption(),
      dokumentUpdatePage.setTextInput('text'),
      dokumentUpdatePage.setDataInput(absolutePath),
      dokumentUpdatePage.setCreatedInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      dokumentUpdatePage.userSelectLastOption(),
    ]);

    expect(await dokumentUpdatePage.getNummerInput()).to.eq('nummer', 'Expected Nummer value to be equals to nummer');
    expect(await dokumentUpdatePage.getTextInput()).to.eq('text', 'Expected Text value to be equals to text');
    expect(await dokumentUpdatePage.getDataInput()).to.endsWith(fileNameToUpload, 'Expected Data value to be end with ' + fileNameToUpload);
    expect(await dokumentUpdatePage.getCreatedInput()).to.contain('2001-01-01T02:30', 'Expected created value to be equals to 2000-12-31');

    await dokumentUpdatePage.save();
    expect(await dokumentUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await dokumentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Dokument', async () => {
    const nbButtonsBeforeDelete = await dokumentComponentsPage.countDeleteButtons();
    await dokumentComponentsPage.clickOnLastDeleteButton();

    dokumentDeleteDialog = new DokumentDeleteDialog();
    expect(await dokumentDeleteDialog.getDialogTitle()).to.eq('kundenportalApp.dokument.delete.question');
    await dokumentDeleteDialog.clickOnConfirmButton();

    expect(await dokumentComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
