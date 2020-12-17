import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { SelfServicesComponentsPage, SelfServicesDeleteDialog, SelfServicesUpdatePage } from './self-services.page-object';
import * as path from 'path';

const expect = chai.expect;

describe('SelfServices e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let selfServicesComponentsPage: SelfServicesComponentsPage;
  let selfServicesUpdatePage: SelfServicesUpdatePage;
  let selfServicesDeleteDialog: SelfServicesDeleteDialog;
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

  it('should load SelfServices', async () => {
    await navBarPage.goToEntity('self-services');
    selfServicesComponentsPage = new SelfServicesComponentsPage();
    await browser.wait(ec.visibilityOf(selfServicesComponentsPage.title), 5000);
    expect(await selfServicesComponentsPage.getTitle()).to.eq('kundenportalApp.selfServices.home.title');
    await browser.wait(
      ec.or(ec.visibilityOf(selfServicesComponentsPage.entities), ec.visibilityOf(selfServicesComponentsPage.noResult)),
      1000
    );
  });

  it('should load create SelfServices page', async () => {
    await selfServicesComponentsPage.clickOnCreateButton();
    selfServicesUpdatePage = new SelfServicesUpdatePage();
    expect(await selfServicesUpdatePage.getPageTitle()).to.eq('kundenportalApp.selfServices.home.createOrEditLabel');
    await selfServicesUpdatePage.cancel();
  });

  it('should create and save SelfServices', async () => {
    const nbButtonsBeforeCreate = await selfServicesComponentsPage.countDeleteButtons();

    await selfServicesComponentsPage.clickOnCreateButton();

    await promise.all([
      selfServicesUpdatePage.serviceTypSelectLastOption(),
      selfServicesUpdatePage.statusSelectLastOption(),
      selfServicesUpdatePage.setTextInput('text'),
      selfServicesUpdatePage.setDateiInput(absolutePath),
      selfServicesUpdatePage.setCreatedInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      selfServicesUpdatePage.userSelectLastOption(),
    ]);

    expect(await selfServicesUpdatePage.getTextInput()).to.eq('text', 'Expected Text value to be equals to text');
    expect(await selfServicesUpdatePage.getDateiInput()).to.endsWith(
      fileNameToUpload,
      'Expected Datei value to be end with ' + fileNameToUpload
    );
    expect(await selfServicesUpdatePage.getCreatedInput()).to.contain(
      '2001-01-01T02:30',
      'Expected created value to be equals to 2000-12-31'
    );

    await selfServicesUpdatePage.save();
    expect(await selfServicesUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await selfServicesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last SelfServices', async () => {
    const nbButtonsBeforeDelete = await selfServicesComponentsPage.countDeleteButtons();
    await selfServicesComponentsPage.clickOnLastDeleteButton();

    selfServicesDeleteDialog = new SelfServicesDeleteDialog();
    expect(await selfServicesDeleteDialog.getDialogTitle()).to.eq('kundenportalApp.selfServices.delete.question');
    await selfServicesDeleteDialog.clickOnConfirmButton();

    expect(await selfServicesComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
