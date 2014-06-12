//
//  AddMovieViewController.m
//  Watch List
//
//  Created by Christopher Rockwell on 6/12/14.
//  Copyright (c) 2014 Christopher Rockwell. All rights reserved.
//

#import "AddMovieViewController.h"
int rowNum;

@interface AddMovieViewController ()

@end

@implementation AddMovieViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    rowNum = 0;
    // white status bar
    [self setNeedsStatusBarAppearanceUpdate];
    
    days = [[NSArray alloc] initWithObjects:@"Sunday", @"Monday", @"Tuesday", @"Wednesday", @"Thursday", @"Friday", @"Saturday", @"Sunday", nil];
    
    self.itemTitle.textField.placeholder = @"Movie/Show Title";
    self.itemTitle.delegate = self;
    [super viewDidLoad];
    // Do any additional setup after loading the view.
}

- (BOOL)textFieldShouldReturn:(UITextField *)textField
{
    return [textField resignFirstResponder];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(UIStatusBarStyle)preferredStatusBarStyle{
    return UIStatusBarStyleLightContent;
}


- (NSInteger)numberOfComponentsInPickerView:(UIPickerView *)pickerView {
    return 1;
}

// returns the # of rows in each component..
- (NSInteger)pickerView:(UIPickerView *)pickerView numberOfRowsInComponent:(NSInteger)component {
    return 7;
}

- (UIView *)pickerView:(UIPickerView *)pickerView viewForRow:(NSInteger)row forComponent:(NSInteger)component reusingView:(UIView *)view {
    
    UILabel *pickerLabel= [[UILabel alloc] initWithFrame:CGRectMake(30.0, 0.0, 150.0, 50.0)];
    [pickerLabel setBackgroundColor:[UIColor clearColor]];
    [pickerLabel setFont:[UIFont boldSystemFontOfSize:20.0]];
    pickerLabel.textAlignment = NSTextAlignmentCenter;
    [pickerLabel setText:[NSString stringWithFormat:@"%@",[days objectAtIndex:row]]];
    
    return pickerLabel;
}

- (void)pickerView:(UIPickerView *)thePickerView didSelectRow:(NSInteger)row inComponent:(NSInteger)component {
    rowNum = row;
}

-(IBAction)onClick:(id)sender {
    UIButton *button = (UIButton *)sender;
    if (button.tag == 0) {
        PFObject *itemInfo = [PFObject objectWithClassName:@"itemInfo"];
        itemInfo[@"user"] = [PFUser currentUser];
        itemInfo[@"title"] = self.itemTitle.textField.text;
        NSDateFormatter *outputFormatter = [[NSDateFormatter alloc] init];
        [outputFormatter setDateFormat:@"h:mm a"];
        itemInfo[@"time"] = [outputFormatter stringFromDate:timePicker.date];;
        itemInfo[@"day"] = [days objectAtIndex:rowNum];
        [itemInfo saveInBackground];
        UIAlertView *alert = [[UIAlertView alloc] initWithTitle:@"Profile Updated"
                                                        message: nil
                                                       delegate:self
                                              cancelButtonTitle:@"OK"
                                              otherButtonTitles:nil];
        [alert show];
    } else {
        [self dismissViewControllerAnimated:true completion:nil];
    }
}

-(void) alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    UIStoryboard *storyBoard = [UIStoryboard storyboardWithName:@"Main" bundle:nil];
    
    AddMovieViewController *watchListView = [storyBoard instantiateViewControllerWithIdentifier:@"WatchListView"];
    [self presentViewController:watchListView animated:true completion:nil];
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
