import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { NachrichtComponentsPage, NachrichtDeleteDialog, NachrichtUpdatePage } from './nachricht.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('Nachricht e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let nachrichtComponentsPage: NachrichtComponentsPage;
  let nachrichtUpdatePage: NachrichtUpdatePage;
  let nachrichtDeleteDialog: NachrichtDeleteDialog;
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

  it('should load Nachrichts', async () => {
    await navBarPage.goToEntity('nachricht');
    nachrichtComponentsPage = new NachrichtComponentsPage();
    await browser.wait(ec.visibilityOf(nachrichtComponentsPage.title), 5000);
    expect(await nachrichtComponentsPage.getTitle()).to.eq('kundenportalApp.nachricht.home.title');
    await browser.wait(ec.or(ec.visibilityOf(nachrichtComponentsPage.entities), ec.visibilityOf(nachrichtComponentsPage.noResult)), 1000);
  });

  it('should load create Nachricht page', async () => {
    await nachrichtComponentsPage.clickOnCreateButton();
    nachrichtUpdatePage = new NachrichtUpdatePage();
    expect(await nachrichtUpdatePage.getPageTitle()).to.eq('kundenportalApp.nachricht.home.createOrEditLabel');
    await nachrichtUpdatePage.cancel();
  });

  it('should create and save Nachrichts', async () => {
    const nbButtonsBeforeCreate = await nachrichtComponentsPage.countDeleteButtons();

    await nachrichtComponentsPage.clickOnCreateButton();

    await promise.all([
      nachrichtUpdatePage.setAnInput('an'),
      nachrichtUpdatePage.setBetreffInput('betreff'),
      nachrichtUpdatePage.setTextInput('text'),
      nachrichtUpdatePage.setAnhangInput(absolutePath),
      nachrichtUpdatePage.statusSelectLastOption(),
      nachrichtUpdatePage.setCreatedInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      nachrichtUpdatePage.userSelectLastOption(),
    ]);

    expect(await nachrichtUpdatePage.getAnInput()).to.eq('an', 'Expected An value to be equals to an');
    expect(await nachrichtUpdatePage.getBetreffInput()).to.eq('betreff', 'Expected Betreff value to be equals to betreff');
    expect(await nachrichtUpdatePage.getTextInput()).to.eq('text', 'Expected Text value to be equals to text');
    expect(await nachrichtUpdatePage.getAnhangInput()).to.endsWith(
      fileNameToUpload,
      'Expected Anhang value to be end with ' + fileNameToUpload
    );
    expect(await nachrichtUpdatePage.getCreatedInput()).to.contain('2001-01-01T02:30', 'Expected created value to be equals to 2000-12-31');

    await nachrichtUpdatePage.save();
    expect(await nachrichtUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await nachrichtComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Nachricht', async () => {
    const nbButtonsBeforeDelete = await nachrichtComponentsPage.countDeleteButtons();
    await nachrichtComponentsPage.clickOnLastDeleteButton();

    nachrichtDeleteDialog = new NachrichtDeleteDialog();
    expect(await nachrichtDeleteDialog.getDialogTitle()).to.eq('kundenportalApp.nachricht.delete.question');
    await nachrichtDeleteDialog.clickOnConfirmButton();

    expect(await nachrichtComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
