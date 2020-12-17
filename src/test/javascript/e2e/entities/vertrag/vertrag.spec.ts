import { browser, ExpectedConditions as ec, protractor, promise } from 'protractor';
import { NavBarPage, SignInPage } from '../../page-objects/jhi-page-objects';

import { VertragComponentsPage, VertragDeleteDialog, VertragUpdatePage } from './vertrag.page-object';

const expect = chai.expect;

describe('Vertrag e2e test', () => {
  let navBarPage: NavBarPage;
  let signInPage: SignInPage;
  let vertragComponentsPage: VertragComponentsPage;
  let vertragUpdatePage: VertragUpdatePage;
  let vertragDeleteDialog: VertragDeleteDialog;

  before(async () => {
    await browser.get('/');
    navBarPage = new NavBarPage();
    signInPage = await navBarPage.getSignInPage();
    await signInPage.autoSignInUsing('admin', 'admin');
    await browser.wait(ec.visibilityOf(navBarPage.entityMenu), 5000);
  });

  it('should load Vertrags', async () => {
    await navBarPage.goToEntity('vertrag');
    vertragComponentsPage = new VertragComponentsPage();
    await browser.wait(ec.visibilityOf(vertragComponentsPage.title), 5000);
    expect(await vertragComponentsPage.getTitle()).to.eq('kundenportalApp.vertrag.home.title');
    await browser.wait(ec.or(ec.visibilityOf(vertragComponentsPage.entities), ec.visibilityOf(vertragComponentsPage.noResult)), 1000);
  });

  it('should load create Vertrag page', async () => {
    await vertragComponentsPage.clickOnCreateButton();
    vertragUpdatePage = new VertragUpdatePage();
    expect(await vertragUpdatePage.getPageTitle()).to.eq('kundenportalApp.vertrag.home.createOrEditLabel');
    await vertragUpdatePage.cancel();
  });

  it('should create and save Vertrags', async () => {
    const nbButtonsBeforeCreate = await vertragComponentsPage.countDeleteButtons();

    await vertragComponentsPage.clickOnCreateButton();

    await promise.all([
      vertragUpdatePage.setVsnrInput('vsnr'),
      vertragUpdatePage.sparteSelectLastOption(),
      vertragUpdatePage.zahlenrhytmusSelectLastOption(),
      vertragUpdatePage.setAntragsdatumInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      vertragUpdatePage.setVersicherungsbeginnInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      vertragUpdatePage.setIbanInput('iban'),
      vertragUpdatePage.setCreatedInput('01/01/2001' + protractor.Key.TAB + '02:30AM'),
      vertragUpdatePage.userSelectLastOption(),
    ]);

    expect(await vertragUpdatePage.getVsnrInput()).to.eq('vsnr', 'Expected Vsnr value to be equals to vsnr');
    expect(await vertragUpdatePage.getAntragsdatumInput()).to.contain(
      '2001-01-01T02:30',
      'Expected antragsdatum value to be equals to 2000-12-31'
    );
    expect(await vertragUpdatePage.getVersicherungsbeginnInput()).to.contain(
      '2001-01-01T02:30',
      'Expected versicherungsbeginn value to be equals to 2000-12-31'
    );
    expect(await vertragUpdatePage.getIbanInput()).to.eq('iban', 'Expected Iban value to be equals to iban');
    expect(await vertragUpdatePage.getCreatedInput()).to.contain('2001-01-01T02:30', 'Expected created value to be equals to 2000-12-31');

    await vertragUpdatePage.save();
    expect(await vertragUpdatePage.getSaveButton().isPresent(), 'Expected save button disappear').to.be.false;

    expect(await vertragComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeCreate + 1, 'Expected one more entry in the table');
  });

  it('should delete last Vertrag', async () => {
    const nbButtonsBeforeDelete = await vertragComponentsPage.countDeleteButtons();
    await vertragComponentsPage.clickOnLastDeleteButton();

    vertragDeleteDialog = new VertragDeleteDialog();
    expect(await vertragDeleteDialog.getDialogTitle()).to.eq('kundenportalApp.vertrag.delete.question');
    await vertragDeleteDialog.clickOnConfirmButton();

    expect(await vertragComponentsPage.countDeleteButtons()).to.eq(nbButtonsBeforeDelete - 1);
  });

  after(async () => {
    await navBarPage.autoSignOut();
  });
});
