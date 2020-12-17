import { element, by, ElementFinder } from 'protractor';

export class VertragComponentsPage {
  createButton = element(by.id('jh-create-entity'));
  deleteButtons = element.all(by.css('jhi-vertrag div table .btn-danger'));
  title = element.all(by.css('jhi-vertrag div h2#page-heading span')).first();
  noResult = element(by.id('no-result'));
  entities = element(by.id('entities'));

  async clickOnCreateButton(): Promise<void> {
    await this.createButton.click();
  }

  async clickOnLastDeleteButton(): Promise<void> {
    await this.deleteButtons.last().click();
  }

  async countDeleteButtons(): Promise<number> {
    return this.deleteButtons.count();
  }

  async getTitle(): Promise<string> {
    return this.title.getAttribute('jhiTranslate');
  }
}

export class VertragUpdatePage {
  pageTitle = element(by.id('jhi-vertrag-heading'));
  saveButton = element(by.id('save-entity'));
  cancelButton = element(by.id('cancel-save'));

  vsnrInput = element(by.id('field_vsnr'));
  sparteSelect = element(by.id('field_sparte'));
  zahlenrhytmusSelect = element(by.id('field_zahlenrhytmus'));
  antragsdatumInput = element(by.id('field_antragsdatum'));
  versicherungsbeginnInput = element(by.id('field_versicherungsbeginn'));
  ibanInput = element(by.id('field_iban'));
  createdInput = element(by.id('field_created'));

  userSelect = element(by.id('field_user'));

  async getPageTitle(): Promise<string> {
    return this.pageTitle.getAttribute('jhiTranslate');
  }

  async setVsnrInput(vsnr: string): Promise<void> {
    await this.vsnrInput.sendKeys(vsnr);
  }

  async getVsnrInput(): Promise<string> {
    return await this.vsnrInput.getAttribute('value');
  }

  async setSparteSelect(sparte: string): Promise<void> {
    await this.sparteSelect.sendKeys(sparte);
  }

  async getSparteSelect(): Promise<string> {
    return await this.sparteSelect.element(by.css('option:checked')).getText();
  }

  async sparteSelectLastOption(): Promise<void> {
    await this.sparteSelect.all(by.tagName('option')).last().click();
  }

  async setZahlenrhytmusSelect(zahlenrhytmus: string): Promise<void> {
    await this.zahlenrhytmusSelect.sendKeys(zahlenrhytmus);
  }

  async getZahlenrhytmusSelect(): Promise<string> {
    return await this.zahlenrhytmusSelect.element(by.css('option:checked')).getText();
  }

  async zahlenrhytmusSelectLastOption(): Promise<void> {
    await this.zahlenrhytmusSelect.all(by.tagName('option')).last().click();
  }

  async setAntragsdatumInput(antragsdatum: string): Promise<void> {
    await this.antragsdatumInput.sendKeys(antragsdatum);
  }

  async getAntragsdatumInput(): Promise<string> {
    return await this.antragsdatumInput.getAttribute('value');
  }

  async setVersicherungsbeginnInput(versicherungsbeginn: string): Promise<void> {
    await this.versicherungsbeginnInput.sendKeys(versicherungsbeginn);
  }

  async getVersicherungsbeginnInput(): Promise<string> {
    return await this.versicherungsbeginnInput.getAttribute('value');
  }

  async setIbanInput(iban: string): Promise<void> {
    await this.ibanInput.sendKeys(iban);
  }

  async getIbanInput(): Promise<string> {
    return await this.ibanInput.getAttribute('value');
  }

  async setCreatedInput(created: string): Promise<void> {
    await this.createdInput.sendKeys(created);
  }

  async getCreatedInput(): Promise<string> {
    return await this.createdInput.getAttribute('value');
  }

  async userSelectLastOption(): Promise<void> {
    await this.userSelect.all(by.tagName('option')).last().click();
  }

  async userSelectOption(option: string): Promise<void> {
    await this.userSelect.sendKeys(option);
  }

  getUserSelect(): ElementFinder {
    return this.userSelect;
  }

  async getUserSelectedOption(): Promise<string> {
    return await this.userSelect.element(by.css('option:checked')).getText();
  }

  async save(): Promise<void> {
    await this.saveButton.click();
  }

  async cancel(): Promise<void> {
    await this.cancelButton.click();
  }

  getSaveButton(): ElementFinder {
    return this.saveButton;
  }
}

export class VertragDeleteDialog {
  private dialogTitle = element(by.id('jhi-delete-vertrag-heading'));
  private confirmButton = element(by.id('jhi-confirm-delete-vertrag'));

  async getDialogTitle(): Promise<string> {
    return this.dialogTitle.getAttribute('jhiTranslate');
  }

  async clickOnConfirmButton(): Promise<void> {
    await this.confirmButton.click();
  }
}
