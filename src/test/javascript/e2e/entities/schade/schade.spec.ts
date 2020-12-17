import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { SchadeComponentsPage, SchadeDeleteDialog, SchadeUpdatePage } from './schade.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Schade e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let schadeComponentsPage: SchadeComponentsPage;
  let schadeUpdatePage: SchadeUpdatePage;
  let schadeDeleteDialog: SchadeDeleteDialog;
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

  it('should load Schades', async () => {
    await navBarPage.goToEntity('schade');
    schadeComponentsPage = new SchadeComponentsPage();
    await browser.wait(ec.visibilityOf(schadeComponentsPage.title), 5000);
    expect(await schadeComponentsPage.getTitle()).to.eq('kundenportalApp.schade.home.title');
    await browser.wait(ec.or(ec.visibilityOf(schadeComponentsPage.entities), ec.visibilityOf(schadeComponentsPage.noResult)), 1000);
  });

  it('should load create Schade page', async () => {
    await schadeComponentsPage.clickOnCreateButton();
    schadeUpdatePage = new SchadeUpdatePage();
    expect(await schadeUpdatePage.getPageTitle()).to.eq('kundenportalApp.schade.home.createOrEditLabel');
    await schadeUpdatePage.cancel();
  });

  it('should create and save Schades', async () => {
    const nbButtonsBeforeCreate = await schadeComponentsPage.countDeleteButtons();

    await schadeComponentsPage.clickOnCreateButton();

    await promise.all([
      schadeUpdatePage.setVsnrInput('vsnr'),
      schadeUpdatePage.setTextInput('text'),
      schadeUpdatePage.setAnhangInput(absolutePath),
      schadeUpdatePage.setCreatedInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      schadeUpdatePage.userSelectLastOption(),
    ]);

    expect(await schadeUpdatePage.getVsnrInput()).to.eq('vsnr', 'Expected Vsnr value to be equals to vsnr');
    expect(await schadeUpdatePage.getTextInput()).to.eq('text', 'Expected Text value to be equals to text');
    expect(await schadeUpdatePage.getAnhangInput()).to.endsWith(
      fileNameToUpload,
      'Expected Anhang value to be end with ' + fileNameToUpload
    );
    expect(await schadeUpdatePage.getCreatedInput()).to.contain('2001-01-01T02:30', 'Expected created value to be equals to 2000-12-31');

    await schadeUpdatePage.save();
    expect(await schadeUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await schadeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Schade', async () => {
    const nbButtonsBeforeDelete = await schadeComponentsPage.countDeleteButtons();
    await schadeComponentsPage.clickOnLastDeleteButton();

    schadeDeleteDialog = new SchadeDeleteDialog();
    expect(await schadeDeleteDialog.getDialogTitle()).to.eq('kundenportalApp.schade.delete.question');
    await schadeDeleteDialog.clickOnConfirmButton();

    expect(await schadeComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
