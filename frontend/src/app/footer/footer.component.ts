import {
  Component,
  inject,
  TemplateRef,
  ViewChild,
} from '@angular/core';
import {
  TuiAlertService,
  TuiAppearance,
  TuiButton,
  TuiDialogService,
  TuiGroup,
} from '@taiga-ui/core';
import {
  FormControl,
  FormsModule,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { TuiInputModule } from '@taiga-ui/legacy';
import { TuiFileLike, TuiFiles } from '@taiga-ui/kit';
import { catchError, finalize, map } from 'rxjs/operators';
import { AsyncPipe, NgIf } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { Observable, of, Subject, switchMap } from 'rxjs';

@Component({
  selector: 'app-footer',
  imports: [
    TuiGroup,
    TuiButton,
    ReactiveFormsModule,
    TuiInputModule,
    TuiFiles,
    FormsModule,
    AsyncPipe,
    NgIf,
    TuiAppearance,
  ],
  templateUrl: './footer.component.html',
  styleUrl: './footer.component.less',
})
export class FooterComponent {
  @ViewChild('importExportDialog')
  private readonly importExportDialog?: TemplateRef<unknown>;

  protected readonly control = new FormControl<TuiFileLike | null>(
    null,
    Validators.required
  );

  private readonly http = inject(HttpClient);
  protected readonly alerts = inject(TuiAlertService);
  private readonly dialogService = inject(TuiDialogService);

  private readonly urlImport = 'http://localhost:8080/catalog/import';
  private readonly urlExport = 'http://localhost:8080/catalog/export';

  protected showDialog(): void {
    if (!this.importExportDialog) return;

    this.dialogService
      .open(this.importExportDialog, {
        label: 'Import/Export',
        size: 's',
      })
      .subscribe({
        complete: () => {
          this.resetDialogState();
        },
      });
  }

  protected readonly failedFiles$ = new Subject<TuiFileLike | null>();
  protected readonly loadingFiles$ =
    new Subject<TuiFileLike | null>();
  protected readonly loadedFiles$ = this.control.valueChanges.pipe(
    switchMap(file => this.processFile(file))
  );

  private resetDialogState(): void {
    this.control.reset();
    this.failedFiles$.next(null);
    this.loadingFiles$.next(null);
  }

  protected removeFile(): void {
    this.control.setValue(null);
    this.failedFiles$.next(null);
    this.loadingFiles$.next(null);
  }

  protected processFile(
    file: TuiFileLike | null
  ): Observable<TuiFileLike | null> {
    if (this.control.invalid || !file) {
      return of(null);
    }

    const isJson =
      file.type === 'application/json' ||
      (file.name && file.name.toLowerCase().endsWith('.json'));

    if (!isJson) {
      this.failedFiles$.next(file);
      return of(null);
    }

    const reader = new FileReader();

    const fileRead$ = new Observable<string>(observer => {
      reader.onload = () => {
        observer.next(reader.result as string);
        observer.complete();
      };
      reader.onerror = error => {
        observer.error(error);
      };
      reader.readAsText(file as File);
    });

    this.loadingFiles$.next(file);

    return fileRead$.pipe(
      switchMap(text => {
        try {
          const json = JSON.parse(text);
          return this.http.post(this.urlImport, json).pipe(
            map(response => {
              this.alerts
                .open(
                  `Импорт успешно выполнен. Импортировано записей: ${response}`,
                  {
                    appearance: 'positive',
                    autoClose: 5000,
                  }
                )
                .subscribe();
              return file;
            }),
            catchError(() => {
              this.failedFiles$.next(file);
              return of(null);
            })
          );
        } catch (e) {
          console.error('Ошибка парсинга JSON', e);
          this.failedFiles$.next(file);
          return of(null);
        }
      }),
      finalize(() => this.loadingFiles$.next(null))
    );
  }

  protected exportData() {
    this.http.get(this.urlExport).subscribe({
      next: (data: any) => {
        const jsonString = JSON.stringify(data, null, 2);
        const blob = new Blob([jsonString], {
          type: 'application/json',
        });
        const url = window.URL.createObjectURL(blob);

        const link = document.createElement('a');
        link.href = url;
        link.download = 'data.json';
        link.click();

        window.URL.revokeObjectURL(url);
      },
      error: (err: unknown) => {
        console.error('Ошибка при отправке формы:', err);
      },
    });
  }
}
